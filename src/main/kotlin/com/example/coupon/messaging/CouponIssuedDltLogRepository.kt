package com.example.coupon.messaging

import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedDltLogRepository : JpaRepository<CouponIssuedDltLog, Long> {
    fun existsByMessageKey(messageKey: String): Boolean
}
