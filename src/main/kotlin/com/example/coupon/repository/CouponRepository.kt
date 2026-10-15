package com.example.coupon.repository

import com.example.coupon.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long>
