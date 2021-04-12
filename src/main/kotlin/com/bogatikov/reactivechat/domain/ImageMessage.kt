package com.bogatikov.reactivechat.domain

import java.time.LocalDateTime
import java.util.*

class ImageMessage(messageId: UUID, chatId: UUID, sender: ChatMember, var imageLink: String, messageDate: LocalDateTime, seen: Boolean) :
    CommonMessage(messageId, chatId, sender, messageDate, seen)