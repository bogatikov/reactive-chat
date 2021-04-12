package com.bogatikov.reactivechat.service.api

import com.bogatikov.reactivechat.domain.CommonMessage
import com.bogatikov.reactivechat.domain.TextMessage
import com.bogatikov.reactivechat.event.NewMessageEvent
import com.bogatikov.reactivechat.event.WebSocketEvent
import reactor.core.publisher.Mono
import java.util.*

interface ChatService {
    fun handleNewMessageEvent(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void>
    fun markPreviousMessagesAsRead(messageId: UUID): Mono<Void>
    fun sendEventToUserId(userId: UUID, webSocketEvent: WebSocketEvent): Mono<Void>
    fun sendMessage(message: CommonMessage): Mono<Void>
    fun broadcastMessage(commonMessage: CommonMessage): Mono<Void>
}
