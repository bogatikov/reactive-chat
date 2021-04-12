package com.example.reactivechat.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
sealed class WebSocketEvent {

    companion object {
        val MAPPER: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
            .enable(SerializationFeature.WRITE_ENUMS_USING_INDEX)
            .apply {
                registerSubtypes(
                    NamedType(NewMessageEvent::class.java, "NewMessageEvent"),
//                    NamedType(NewChatEvent::class.java, "NewChatEvent"),
                    NamedType(MarkMessageAsRead::class.java, "MarkMessageAsRead")
                )
            }
    }

    fun toJson(): String = MAPPER.writeValueAsString(this)
}


data class NewMessageEvent(val chatId: UUID, val content: String) : WebSocketEvent()

//data class ReceivedMessageEvent(val message: CommonMessage) : WebSocketEvent()
//data class NewChatEvent(val chat: Chat) : WebSocketEvent()
data class MessageSendEvent(val msg: String) : WebSocketEvent()
data class MarkMessageAsRead(val chatId: UUID?, val messageId: UUID) : WebSocketEvent()
