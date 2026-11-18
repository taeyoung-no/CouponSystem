package com.example.gateway

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Component
class AuthenticationFilter(@Value("\${jwt.secret}") secret: String) : GlobalFilter, Ordered {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request.mutate()
            .headers {
                it.remove(MEMBER_ID_HEADER)
                it.remove(MEMBER_ROLE_HEADER)
            }
            .build()

        val claims = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.let { parse(it) }

        if (claims == null) {
            if (request.path.value().startsWith(PUBLIC_PATH_PREFIX)) {
                return chain.filter(exchange.mutate().request(request).build())
            }
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }

        val authenticated = request.mutate()
            .header(MEMBER_ID_HEADER, claims.subject)
            .header(MEMBER_ROLE_HEADER, claims["role", String::class.java])
            .build()
        return chain.filter(exchange.mutate().request(authenticated).build())
    }

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE

    private fun parse(token: String): Claims? {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        const val MEMBER_ID_HEADER = "Member-Id"
        const val MEMBER_ROLE_HEADER = "Member-Role"
        private const val PUBLIC_PATH_PREFIX = "/api/member/"
    }
}