package com.example.reactivechat.config

import com.example.reactivechat.handler.SendTo
import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
class SinkWrapper {
    val sinks: Sinks.Many<SendTo> = Sinks.many().multicast().onBackpressureBuffer()
}