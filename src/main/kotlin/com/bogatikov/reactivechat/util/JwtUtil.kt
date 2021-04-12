package com.bogatikov.reactivechat.util

import io.jsonwebtoken.*
import org.slf4j.Logger
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    val logger: Logger
) {

    private val secret: String = Base64.getEncoder()
        .encodeToString("14wcjuT4R2GOZ1Bu1YMZ6AjMs5Ffgl8aMtH0wzKNwhtLxM2cZ3gaRNdrv4z9Jo1fQHAI54kamyeJxGOAIFFnvSpCW27BhA9v4Vv6UJPVRqPZwgSxM5bUqLeM".toByteArray())

    fun extractUsername(authToken: String): String {
        return getClaimsFromToken(authToken)
            .subject
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
            return true
        } catch (ex: SecurityException) {
            logger.error("Security exception", ex)
        } catch (ex: MalformedJwtException) {
            logger.error("MalformedJwtException exception", ex)
        } catch (ex: ExpiredJwtException) {
            logger.error("ExpiredJwtException exception", ex)
        } catch (ex: UnsupportedJwtException) {
            logger.error("UnsupportedJwtException exception", ex)
        } catch (ex: IllegalArgumentException) {
            logger.error("IllegalArgumentException exception", ex)
        }
        return false
    }
}