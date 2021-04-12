package com.bogatikov.reactivechat.service

import com.bogatikov.reactivechat.config.SinkWrapper
import com.bogatikov.reactivechat.domain.ChatMember
import com.bogatikov.reactivechat.domain.CommonMessage
import com.bogatikov.reactivechat.domain.TextMessage
import com.bogatikov.reactivechat.event.ChatMessageEvent
import com.bogatikov.reactivechat.event.NewMessageEvent
import com.bogatikov.reactivechat.event.WebSocketEvent
import com.bogatikov.reactivechat.handler.SendTo
import com.bogatikov.reactivechat.messeging.RedisChatMessagePublisher
import com.bogatikov.reactivechat.repository.ChatRepository
import com.bogatikov.reactivechat.service.api.ChatService
import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.LocalDateTime
import java.util.*

@Service
class DefaultChatService(
    val logger: Logger,
    val sinkWrapper: SinkWrapper,
    val chatRepository: ChatRepository,
    val redisChatPublisher: RedisChatMessagePublisher
) : ChatService {

    override fun handleNewMessageEvent(senderId: UUID, newMessageEvent: NewMessageEvent): Mono<Void> {
        logger.info("Receive NewMessageEvent from $senderId: $newMessageEvent")
        return chatRepository.findById(newMessageEvent.chatId)
            .filter { it.chatMembers.map(ChatMember::userId).contains(senderId) }
            .flatMap { chat ->
                val textMessage = TextMessage(UUID.randomUUID(), chat.chatId, chat.chatMembers.first { it.userId == senderId }, newMessageEvent.content, LocalDateTime.now(), false)
                chat.lastMessage = textMessage
                return@flatMap Mono.zip(chatRepository.save(chat), Mono.just(textMessage))
            }
            .flatMap { broadcastMessage(it.t2) }
    }

    /**
     * Broadcast the message between instances
     */
    override fun broadcastMessage(commonMessage: CommonMessage): Mono<Void> {
        return redisChatPublisher.broadcastMessage(commonMessage)
    }

    /**
     * Send the message to all of chatMembers of message chat direct
     */
    override fun sendMessage(message: CommonMessage): Mono<Void> {
        return chatRepository.findById(message.chatId)
            .map { it.chatMembers }
            .flatMapMany { Flux.fromIterable(it) }
            .flatMap { member -> sendEventToUserId(member.userId, ChatMessageEvent(message.chatId, message)) }
            .then()
    }

    override fun markPreviousMessagesAsRead(messageId: UUID): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun sendEventToUserId(userId: UUID, webSocketEvent: WebSocketEvent): Mono<Void> {
        return Mono.fromCallable { sinkWrapper.sinks.emitNext(SendTo(userId, webSocketEvent), Sinks.EmitFailureHandler.FAIL_FAST) }
            .then()
    }
}