package com.example.coupon.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_issued_coupon_member_coupon",
            columnNames = ["member_id", "coupon_id"],
        ),
    ],
)
class IssuedCoupon(
    var memberId: Long,
    var couponId: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)
