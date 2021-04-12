package com.bogatikov.reactivechat.event

import com.bogatikov.reactivechat.domain.CommonMessage
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
sealed class WebSocketEvent


data class NewMessageEvent(val chatId: UUID, val content: String) : WebSocketEvent()
data class ChatMessageEvent(val chatId: UUID, val payload: CommonMessage) : WebSocketEvent()
data class MessageSendEvent(val msg: String) : WebSocketEvent()
data class MarkMessageAsRead(val chatId: UUID?, val messageId: UUID) : WebSocketEvent()
