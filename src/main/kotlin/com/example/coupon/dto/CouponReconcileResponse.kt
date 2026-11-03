package com.example.coupon.dto

class CouponReconcileResponse(
    val couponId: Long,
    val redisCount: Long?,
    val dbCount: Int?,
    val redisIssued: Long,
    val dbIssued: Long,
    val countMatched: Boolean,
    val issuedMatched: Boolean,
)
