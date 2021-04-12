package com.bogatikov.reactivechat.security

import com.bogatikov.reactivechat.util.JwtUtil
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class JwtAuthenticationManager(val jwtUtil: JwtUtil) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val token = authentication.credentials.toString()
        val validateToken = jwtUtil.validateToken(token)
        var username: String?
        try {
            username = jwtUtil.extractUsername(token)
        } catch (e: Exception) {
            username = null
            println(e)
        }
        return if (username != null && validateToken) {
            val claims = jwtUtil.getClaimsFromToken(token)
            val roles: List<String> = claims["roles"] as List<String>
            val authorities = roles.stream()
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
            val authenticationToken = UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
            )
            authenticationToken.details = claims
            Mono.just(authenticationToken)
        } else {
            Mono.empty()
        }
    }
}