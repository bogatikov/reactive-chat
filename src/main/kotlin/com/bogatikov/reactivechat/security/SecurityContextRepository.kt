package com.bogatikov.reactivechat.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(val authenticationManager: ReactiveAuthenticationManager) : ServerSecurityContextRepository {
    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        return Mono.error { IllegalStateException("Save method not supported") }
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val authHeader = exchange.request
            .headers
            .getFirst(HttpHeaders.AUTHORIZATION)

        val accessToken: String = if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)

        } else exchange.request
            .queryParams
            .getFirst("access_token") ?: return Mono.empty()

        val auth = UsernamePasswordAuthenticationToken(accessToken, accessToken)
        return authenticationManager
            .authenticate(auth)
            .map { authentication: Authentication -> SecurityContextImpl(authentication) }
    }
}