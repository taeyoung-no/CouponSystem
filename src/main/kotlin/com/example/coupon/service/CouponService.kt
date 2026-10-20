package com.example.coupon.service

import com.example.coupon.dto.CouponResponse
import com.example.coupon.dto.IssuedCouponResponse
import com.example.coupon.entity.Coupon
import com.example.coupon.entity.IssuedCoupon
import com.example.coupon.repository.CouponRepository
import com.example.coupon.repository.IssuedCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
    private val couponIssuer: CouponIssuer,
) {

    fun create(count: Int): CouponResponse {
        val saved = couponRepository.save(Coupon(count))
        val id = requireNotNull(saved.id)
        couponIssuer.init(id, saved.count)
        return CouponResponse(id, saved.count)
    }

    @Transactional
    fun issue(memberId: Long, couponId: Long): IssuedCouponResponse {
        if (!couponRepository.existsById(couponId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        couponIssuer.tryIssue(couponId)
        couponRepository.decrementCount(couponId)
        val saved = issuedCouponRepository.save(IssuedCoupon(memberId, couponId))
        return IssuedCouponResponse(requireNotNull(saved.id), memberId, couponId)
    }
}
