package com.example.coupon.controller

import com.example.coupon.dto.CouponReconcileResponse
import com.example.coupon.dto.CouponResponse
import com.example.coupon.dto.IssuedCouponResponse
import com.example.coupon.service.CouponReconcileService
import com.example.coupon.service.CouponService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupon")
class CouponController(
    private val couponService: CouponService,
    private val couponReconcileService: CouponReconcileService,
) {

    @PostMapping
    fun create(@RequestParam count: Int): CouponResponse = couponService.create(count)

    @PostMapping("/{id}/issue")
    fun issue(
        @PathVariable id: Long,
        authentication: Authentication,
    ): IssuedCouponResponse = couponService.issue(authentication.principal as Long, id)

    @GetMapping("{id}/reconcile")
    fun reconcile(@PathVariable id: Long): CouponReconcileResponse = couponReconcileService.report(id)
}
