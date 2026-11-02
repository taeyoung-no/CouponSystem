package com.example.coupon.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

enum class MemberRole {
    USER,
    ADMIN,
}

@Entity
class Member(
    var username: String,
    var password: String,
    @Enumerated(EnumType.STRING)
    var role: MemberRole = MemberRole.USER,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)
