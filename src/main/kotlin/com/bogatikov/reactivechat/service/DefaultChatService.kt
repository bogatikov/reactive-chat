package com.bogatikov.reactivechat.service

import com.bogatikov.reactivechat.config.SinkWrapper
import com.bogatikov.reactivechat.event.NewMessageEvent
import com.bogatikov.reactivechat.event.WebSocketEvent
import com.bogatikov.reactivechat.handler.SendTo
import com.bogatikov.reactivechat.service.api.ChatService
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