package com.example.coupon.service

import com.example.coupon.dto.CouponResponse
import com.example.coupon.entity.Coupon
import com.example.coupon.repository.CouponRepository
import org.springframework.stereotype.Service

@Service
class CouponService(private val couponRepository: CouponRepository) {

    fun create(count: Int): CouponResponse {
        val saved = couponRepository.save(Coupon(count))
        return CouponResponse(requireNotNull(saved.id), saved.count)
    }
}
