package com.example.reactivechat.service

import com.example.reactivechat.event.NewMessageEvent
import com.example.reactivechat.service.api.ChatService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DefaultChatService(
    val logger: Logger
) : ChatService {
    override fun handleNewMessageEvent(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void> {
        logger.info("Receive NewMessageEvent from $senderId: $newMessageEvent")
        return Mono.empty<Void?>().then()
    }

    override fun markPreviousMessagesAsRead(messageId: UUID): Mono<Void> {
        TODO("Not yet implemented")
    }
}