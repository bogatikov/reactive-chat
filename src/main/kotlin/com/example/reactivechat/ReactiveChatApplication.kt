package com.example.reactivechat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
class ReactiveChatApplication

fun main(args: Array<String>) {
    runApplication<ReactiveChatApplication>(*args)


}
