package com.example.coupon.service

import com.example.coupon.dto.MemberResponse
import com.example.coupon.entity.Member
import com.example.coupon.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository) {

    fun signup(username: String, password: String): MemberResponse {
        val saved = memberRepository.save(Member(username, password))
        return MemberResponse(requireNotNull(saved.id), saved.username)
    }
}