package com.example.coupon.service

import com.example.coupon.auth.JwtProvider
import com.example.coupon.dto.LoginResponse
import com.example.coupon.dto.MemberResponse
import com.example.coupon.entity.Member
import com.example.coupon.repository.MemberRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val jwtProvider: JwtProvider,
) {

    fun signup(username: String, password: String): MemberResponse {
        val saved = memberRepository.save(Member(username, password))
        return MemberResponse(requireNotNull(saved.id), saved.username)
    }

    fun login(username: String, password: String): LoginResponse {
        val member = memberRepository.findByUsername(username)
        if (member?.password != password) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "뭔가 잘못 입력함")
        }
        return LoginResponse(jwtProvider.createToken(requireNotNull(member.id)))
    }
}