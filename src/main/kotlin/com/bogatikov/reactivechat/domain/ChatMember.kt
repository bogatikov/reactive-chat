package com.bogatikov.reactivechat.domain

import java.util.*

data class ChatMember(
    val userId: UUID,
    var fullName: String,
    var avatar: String,
    var deleteChat: Boolean
)
