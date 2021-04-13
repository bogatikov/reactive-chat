package com.bogatikov.reactivechat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveChatApplication

fun main(args: Array<String>) {
    runApplication<ReactiveChatApplication>(*args)
}
