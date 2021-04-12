package com.example.reactivechat.handler

import com.example.reactivechat.config.SinkWrapper
import com.example.reactivechat.event.MarkMessageAsRead
import com.example.reactivechat.event.NewMessageEvent
import com.example.reactivechat.event.WebSocketEvent
import com.example.reactivechat.service.api.ChatService
import com.example.reactivechat.util.ObjectStringConverter
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import org.slf4j.Logger
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatWebSocketHandler(
    val objectMapper: ObjectMapper,
    val logger: Logger,
    val chatService: ChatService,
    val objectStringConverter: ObjectStringConverter,
    val sinkWrapper: SinkWrapper
) : WebSocketHandler {

    private val userIdToSession: MutableMap<UUID, LinkedList<WebSocketSession>> = ConcurrentHashMap()

    override fun handle(session: WebSocketSession): Mono<Void> {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { ctx ->
                val userId = UUID.fromString((ctx.authentication.details as Claims)["id"].toString())
                val sendMessage = sinkWrapper.sinks.asFlux()
                    .filter { sendTo -> sendTo.userId == userId }
                    .map { sendTo -> objectMapper.writeValueAsString(sendTo.event) }
                    .map { stringObject -> session.textMessage(stringObject) }
                    .doOnError { logger.error("", it) }
                val sender = session.send(sendMessage)
                val receiver = session.receive()
                    .filter { it.type == WebSocketMessage.Type.TEXT }
                    .map(WebSocketMessage::getPayloadAsText)
                    .flatMap {
                        objectStringConverter.stringToObject(it, WebSocketEvent::class.java)
                    }
                    .flatMap { convertedEvent ->
                        when (convertedEvent) {
                            is NewMessageEvent -> chatService.handleNewMessageEvent(userId, convertedEvent)
                            is MarkMessageAsRead -> chatService.markPreviousMessagesAsRead(convertedEvent.messageId)
                            else -> Mono.error(RuntimeException())
                        }
                    }
                    .onErrorContinue { t, _ -> logger.error("Error occurred with receiver stream", t) }
                    .doOnSubscribe {
                        val userSession = userIdToSession[userId]
                        if (userSession == null) {
                            val newUserSessions = LinkedList<WebSocketSession>()
                            userIdToSession[userId] = newUserSessions
                        }
                        userIdToSession[userId]?.add(session)
                    }
                    .doFinally {
                        val userSessions = userIdToSession[userId]
                        userSessions?.remove(session)
                    }
                    .then()

                return@flatMap Mono.zip(sender, receiver).then()
            }
    }
}

data class SendTo(
    val userId: UUID,
    val event: WebSocketEvent
)
