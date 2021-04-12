package com.example.reactivechat.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ObjectStringConverter(val objectMapper: ObjectMapper) {

    fun <T> stringToObject(data: String?, clazz: Class<T>): Mono<T> {
        return Mono.fromCallable { objectMapper.readValue(data, clazz) }
            .doOnError { println("Error converting [${data}] to class '${clazz.simpleName}'.") }
    }

    fun <T> objectToString(`object`: T): Mono<String> {
        return Mono.fromCallable { objectMapper.writeValueAsString(`object`) }
            .doOnError { println("Error converting [${`object`}] to String.") }
    }
}