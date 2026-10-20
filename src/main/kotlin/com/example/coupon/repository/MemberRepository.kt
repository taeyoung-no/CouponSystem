package com.example.coupon.repository

import com.example.coupon.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByUsername(username: String): Member?
}
