package com.example.coupon.messaging

import com.example.coupon.entity.IssuedCoupon
import com.example.coupon.repository.CouponRepository
import com.example.coupon.repository.IssuedCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponIssuedWriter(
    private val couponRepository: CouponRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
) {

    @Transactional
    fun write(event: CouponIssued) {
        couponRepository.decrementCount(event.couponId)
        issuedCouponRepository.save(IssuedCoupon(event.memberId, event.couponId))
    }
}
