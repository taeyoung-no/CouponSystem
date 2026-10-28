package com.example.coupon.service

import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class CouponIssuer(private val redisTemplate: StringRedisTemplate) {

    private val script: RedisScript<Long> = RedisScript.of(
        ClassPathResource("issue.lua"),
        Long::class.java,
    )

    fun init(couponId: Long, count: Int) {
        redisTemplate.opsForValue().set(getCountKey(couponId), count.toString())
    }

    fun tryIssue(couponId: Long, memberId: Long) {
        val raw = redisTemplate.execute(
            script,
            listOf(getCountKey(couponId), getMembersKey(couponId)),
            memberId.toString(),
        ) ?: error("Lua 스크립트 문제인 듯")

        when (raw) {
            -1L -> throw ResponseStatusException(HttpStatus.CONFLICT)
            0L -> throw ResponseStatusException(HttpStatus.CONFLICT)
            1L -> Unit
            else -> error("Lua 스크립트 문제인 듯")
        }
    }

    private fun getCountKey(couponId: Long) = "coupon:$couponId:count"

    private fun getMembersKey(couponId: Long) = "coupon:$couponId:members"
}