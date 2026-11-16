package com.example.coupon.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SoldOutChecker(private val couponIssuer: CouponIssuer) {

    private val cache: LoadingCache<Long, Boolean> = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMillis(TTL_MS))
        .maximumSize(MAX_SIZE)
        .build { couponId ->
            val count = couponIssuer.getCount(couponId)
            count != null && count <= 0
        }

    fun isSoldOut(couponId: Long): Boolean = cache.get(couponId) ?: false

    private companion object {
        const val TTL_MS = 1_000L
        const val MAX_SIZE = 1_000L
    }
}
