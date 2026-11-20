package com.example.coupon.service

import com.example.coupon.dto.CouponReconcileResponse
import com.example.coupon.repository.CouponRepository
import com.example.coupon.repository.IssuedCouponRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CouponReconcileService(
    private val couponRepository: CouponRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
    private val redisTemplate: StringRedisTemplate,
    @Value("\${coupon.reconcile.interval-ms}") private val intervalMs: Long,
) {

    fun report(couponId: Long): CouponReconcileResponse {
        val coupon = couponRepository.findById(couponId).orElse(null)
        val redisCount = redisTemplate.opsForValue().get("coupon:$couponId:count")?.toLong()
        val dbCount = coupon?.count
        val redisIssued = redisTemplate.opsForSet().size("coupon:$couponId:members") ?: 0
        val dbIssued = issuedCouponRepository.countByCouponId(couponId)

        return CouponReconcileResponse(
            couponId,
            redisCount,
            dbCount,
            redisIssued,
            dbIssued,
            redisCount != null && redisCount == dbCount?.toLong(),
            redisIssued == dbIssued,
        )
    }

    fun reconcile(couponId: Long): CouponReconcileResponse {
        val issued = issuedCouponRepository.findByCouponId(couponId)
        if (issued.isNotEmpty()) {
            redisTemplate.opsForSet().add(
                "coupon:$couponId:members",
                *issued.map { it.memberId.toString() }.toTypedArray(),
            )
        }
        return report(couponId)
    }

    @Scheduled(fixedRateString = "\${coupon.reconcile.interval-ms}")
    fun recent() {
        val end = System.currentTimeMillis()
        val start = end - intervalMs * 2
        val couponIds = redisTemplate.opsForZSet().rangeByScore(
            "coupon:reconcile:recent",
            start.toDouble(),
            end.toDouble(),
        ) ?: emptySet()

        couponIds.mapNotNull { it.toLongOrNull() }.forEach { reconcile(it) }
    }
}
