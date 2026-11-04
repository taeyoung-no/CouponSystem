package com.example.coupon.repository

import com.example.coupon.entity.IssuedCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface IssuedCouponRepository : JpaRepository<IssuedCoupon, Long> {

    fun countByCouponId(couponId: Long): Long

    fun findByCouponId(couponId: Long): List<IssuedCoupon>
}
