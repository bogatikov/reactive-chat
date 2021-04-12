package com.example.reactivechat.service

import com.example.reactivechat.event.NewMessageEvent
import com.example.reactivechat.service.api.ChatService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DefaultChatService : ChatService {
    override fun handle(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void> {
        TODO("Not yet implemented")
    }
}