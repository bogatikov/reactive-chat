package com.example.reactivechat.service.api

import com.example.reactivechat.event.NewMessageEvent
import reactor.core.publisher.Mono
import java.util.*

interface ChatService {
    fun handleNewMessageEvent(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void>
    fun markPreviousMessagesAsRead(messageId: UUID): Mono<Void>
}
