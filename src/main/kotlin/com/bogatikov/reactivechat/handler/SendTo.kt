package com.bogatikov.reactivechat.handler

import com.bogatikov.reactivechat.event.WebSocketEvent
import java.util.*

data class SendTo(
    val userId: UUID,
    val event: WebSocketEvent
)