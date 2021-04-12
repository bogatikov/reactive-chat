package com.bogatikov.reactivechat.domain

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
open class CommonMessage(
    val messageId: UUID,
    val chatId: UUID,
    val sender: ChatMember,
    @field:JsonSerialize(using = LocalDateTimeSerializer::class) @field:JsonDeserialize(using = LocalDateTimeDeserializer::class) val messageDate: LocalDateTime,
    var seen: Boolean
)
