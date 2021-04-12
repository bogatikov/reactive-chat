package com.bogatikov.reactivechat.repository

import com.bogatikov.reactivechat.domain.Chat
import reactor.core.publisher.Mono
import java.util.*

interface ChatRepository {
    fun findById(id: UUID): Mono<Chat>
    fun save(chat: Chat): Mono<Chat>
}
