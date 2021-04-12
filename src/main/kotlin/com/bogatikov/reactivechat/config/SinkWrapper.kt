package com.bogatikov.reactivechat.config

import com.bogatikov.reactivechat.handler.SendTo
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
class SinkWrapper {
    val sinks: Sinks.Many<SendTo> = Sinks.many().multicast().onBackpressureBuffer()
}