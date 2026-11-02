package com.example.coupon.messaging

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/coupon/dlt")
class CouponIssuedDltController(
    private val couponIssuedDltReplayService: CouponIssuedDltReplayService,
) {

    @PostMapping("/{id}/replay")
    fun replay(@PathVariable id: Long) = couponIssuedDltReplayService.replay(id)
}
