package com.bogatikov.reactivechat.config

import com.bogatikov.reactivechat.domain.ImageMessage
import com.bogatikov.reactivechat.domain.TextMessage
import com.bogatikov.reactivechat.event.MarkMessageAsRead
import com.bogatikov.reactivechat.event.NewMessageEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .registerModule(ParameterNamesModule())
        .registerModule(KotlinModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .apply {
            registerSubtypes(
                NamedType(NewMessageEvent::class.java, "NewMessageEvent"),
                NamedType(MarkMessageAsRead::class.java, "MarkMessageAsRead"),
                NamedType(TextMessage::class.java, "TextMessage"),
                NamedType(ImageMessage::class.java, "ImageMessage")
            )
        }
}