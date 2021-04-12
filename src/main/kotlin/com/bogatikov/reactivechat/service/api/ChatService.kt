package com.bogatikov.reactivechat.service.api

import com.bogatikov.reactivechat.event.NewMessageEvent
import com.bogatikov.reactivechat.event.WebSocketEvent
import reactor.core.publisher.Mono
import java.util.*

interface ChatService {
    fun handleNewMessageEvent(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void>
    fun markPreviousMessagesAsRead(messageId: UUID): Mono<Void>
    fun sendMessageToUserId(userId: UUID, webSocketEvent: WebSocketEvent)
}
