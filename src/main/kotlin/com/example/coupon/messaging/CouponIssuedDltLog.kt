package com.example.coupon.messaging

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

enum class CouponIssuedDltStatus {
    PENDING,
    REPLAYED,
}

@Entity
class CouponIssuedDltLog(
    var messageKey: String,
    @Column(columnDefinition = "TEXT")
    var payload: String,
    @Column(columnDefinition = "TEXT")
    var errorMessage: String? = null,
    @Enumerated(EnumType.STRING)
    var status: CouponIssuedDltStatus = CouponIssuedDltStatus.PENDING,
    var receivedAt: LocalDateTime,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)
