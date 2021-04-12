package com.example.reactivechat.service

import com.example.reactivechat.config.SinkWrapper
import com.example.reactivechat.event.NewMessageEvent
import com.example.reactivechat.event.WebSocketEvent
import com.example.reactivechat.handler.SendTo
import com.example.reactivechat.service.api.ChatService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.util.*

@Service
class DefaultChatService(
    val logger: Logger,
    val sinkWrapper: SinkWrapper
) : ChatService {
    override fun handleNewMessageEvent(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void> {
        logger.info("Receive NewMessageEvent from $senderId: $newMessageEvent")
        sendMessageToUserId(senderId, newMessageEvent)
        return Mono.empty<Void?>().then()
    }

    override fun markPreviousMessagesAsRead(messageId: UUID): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun sendMessageToUserId(userId: UUID, webSocketEvent: WebSocketEvent) {
        sinkWrapper.sinks.emitNext(SendTo(userId, webSocketEvent), Sinks.EmitFailureHandler.FAIL_FAST)
    }
}