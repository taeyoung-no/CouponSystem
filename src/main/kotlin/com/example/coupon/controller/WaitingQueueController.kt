package com.example.coupon.controller

import com.example.coupon.dto.WaitingQueueResponse
import com.example.coupon.service.WaitingQueueService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/waiting-queue")
class WaitingQueueController(private val waitingQueueService: WaitingQueueService) {

    @PostMapping("/{couponId}")
    fun enqueue(
        @PathVariable couponId: Long,
        authentication: Authentication,
    ): WaitingQueueResponse = waitingQueueService.enqueue(couponId, authentication.principal as Long)

    @GetMapping("/{couponId}")
    fun check(
        @PathVariable couponId: Long,
        authentication: Authentication,
    ): WaitingQueueResponse = waitingQueueService.check(couponId, authentication.principal as Long)
}
