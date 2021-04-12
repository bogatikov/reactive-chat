package com.example.reactivechat.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ObjectStringConverter(
    val objectMapper: ObjectMapper,
    val logger: Logger
) {

    fun <T> stringToObject(data: String?, clazz: Class<T>): Mono<T> {
        return Mono.fromCallable { objectMapper.readValue(data, clazz) }
            .doOnError { logger.error("Error converting [${data}] to class '${clazz.simpleName}'.", it) }
    }

    fun <T> objectToString(`object`: T): Mono<String> {
        return Mono.fromCallable { objectMapper.writeValueAsString(`object`) }
            .doOnError { logger.error("Error converting [${`object`}] to String.", it) }
    }
}