package com.example.coupon.repository

import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Repository

@Repository
class WaitingQueueRepository(private val redisTemplate: StringRedisTemplate) {

    private val enqueueScript: RedisScript<Long> = RedisScript.of(
        ClassPathResource("enqueue.lua"),
        Long::class.java,
    )

    private val checkScript: RedisScript<Long> = RedisScript.of(
        ClassPathResource("check-queue.lua"),
        Long::class.java,
    )

    private val dequeueScript: RedisScript<Long> = RedisScript.of(
        ClassPathResource("dequeue.lua"),
        Long::class.java,
    )

    fun enqueue(couponId: Long, memberId: Long): Long {
        redisTemplate.opsForSet().add(QUEUES_KEY, couponId.toString())
        return redisTemplate.execute(
            enqueueScript,
            listOf(getQueueKey(couponId), getPassKey(couponId, memberId)),
            memberId.toString(),
        ) ?: error("Lua 스크립트 문제인 듯")
    }

    fun check(couponId: Long, memberId: Long): Long {
        return redisTemplate.execute(
            checkScript,
            listOf(getQueueKey(couponId), getPassKey(couponId, memberId)),
            memberId.toString(),
        ) ?: error("Lua 스크립트 문제인 듯")
    }

    fun getActiveCouponIds(): List<Long> {
        return redisTemplate.opsForSet().members(QUEUES_KEY).orEmpty().mapNotNull { it.toLongOrNull() }
    }

    fun dequeue(couponId: Long, count: Int, passTtlMs: Long): Long {
        return redisTemplate.execute(
            dequeueScript,
            listOf(getQueueKey(couponId)),
            count.toString(),
            passTtlMs.toString(),
            getPassKeyPrefix(couponId),
        ) ?: error("Lua 스크립트 문제인 듯")
    }

    fun isAdmitted(couponId: Long, memberId: Long): Boolean {
        return redisTemplate.hasKey(getPassKey(couponId, memberId))
    }

    private fun getQueueKey(couponId: Long) = "waiting:$couponId:queue"

    private fun getPassKeyPrefix(couponId: Long) = "waiting:$couponId:pass:"

    private fun getPassKey(couponId: Long, memberId: Long) = getPassKeyPrefix(couponId) + memberId

    private companion object {
        const val QUEUES_KEY = "waiting:queues"
    }
}
