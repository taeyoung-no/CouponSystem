package com.example.coupon.controller

import com.example.coupon.service.CouponService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupon")
class CouponController(private val couponService: CouponService) {

    @PostMapping
    fun create(count: Int) = couponService.create(count)
}
