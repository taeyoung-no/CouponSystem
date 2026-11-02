package com.example.coupon.auth

import com.example.coupon.entity.MemberRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

data class JwtPrincipal(
    val memberId: Long,
    val role: MemberRole,
)

@Component
class JwtProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.expiration-ms}") private val expirationMs: Long,
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun createToken(memberId: Long, role: MemberRole): String {
        return Jwts.builder()
            .subject(memberId.toString())
            .claim("role", role.name)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(key)
            .compact()
    }

    fun parse(token: String): JwtPrincipal {
        val payload = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
        return JwtPrincipal(
            memberId = payload.subject.toLong(),
            role = MemberRole.valueOf(payload["role", String::class.java]),
        )
    }
}
