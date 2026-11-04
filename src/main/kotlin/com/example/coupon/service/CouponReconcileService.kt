package com.example.coupon.service

import com.example.coupon.dto.CouponReconcileResponse
import com.example.coupon.repository.CouponRepository
import com.example.coupon.repository.IssuedCouponRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class CouponReconcileService(
    private val couponRepository: CouponRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
    private val redisTemplate: StringRedisTemplate,
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
}
