package com.example.reactivechat.service.api

import com.example.reactivechat.event.NewMessageEvent
import reactor.core.publisher.Mono
import java.util.*

interface ChatService {
    fun handle(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void>
}
