package com.example.coupon.controller

import com.example.coupon.service.CouponService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupon")
class CouponController(private val couponService: CouponService) {

    @PostMapping
    fun create(count: Int) = couponService.create(count)

    @PostMapping("/{id}/issue")
    fun issue(
        @PathVariable id: Long,
        authentication: Authentication,
    ) = couponService.issue(authentication.principal as Long, id)
}
