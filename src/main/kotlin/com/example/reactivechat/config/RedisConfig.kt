package com.example.reactivechat.config

import org.slf4j.Logger
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate

@Configuration
class RedisConfig {

    @Bean
    fun reactiveRedisConnectionFactory(redisProperties: RedisProperties): ReactiveRedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)
        redisStandaloneConfiguration.setPassword(redisProperties.password)
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun template(reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory): ReactiveStringRedisTemplate {
        return ReactiveStringRedisTemplate(reactiveRedisConnectionFactory)
    }
}