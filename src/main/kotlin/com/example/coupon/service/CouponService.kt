package com.example.coupon.service

import com.example.coupon.dto.CouponResponse
import com.example.coupon.dto.IssuedCouponResponse
import com.example.coupon.entity.Coupon
import com.example.coupon.exception.CouponNotFoundException
import com.example.coupon.exception.ShouldWaitException
import com.example.coupon.exception.SoldOutException
import com.example.coupon.messaging.CouponIssued
import com.example.coupon.messaging.CouponIssuedProducer
import com.example.coupon.repository.CouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponIssuer: CouponIssuer,
    private val couponIssuedProducer: CouponIssuedProducer,
    private val soldOutChecker: SoldOutChecker,
    private val waitingQueueService: WaitingQueueService,
) {

    fun create(count: Int): CouponResponse {
        val saved = couponRepository.save(Coupon(count))
        val id = requireNotNull(saved.id)
        couponIssuer.init(id, saved.count)
        return CouponResponse(id, saved.count)
    }

    fun issue(memberId: Long, couponId: Long): IssuedCouponResponse {
        if (soldOutChecker.isSoldOut(couponId)) {
            throw SoldOutException()
        }
        if (!waitingQueueService.isAdmitted(couponId, memberId)) {
            throw ShouldWaitException()
        }
        if (!couponRepository.existsById(couponId)) {
            throw CouponNotFoundException()
        }
        couponIssuer.tryIssue(couponId, memberId)
        couponIssuedProducer.publish(CouponIssued(memberId, couponId))
        return IssuedCouponResponse(memberId, couponId)
    }
}
