package com.example.coupon.controller

import com.example.coupon.dto.LoginResponse
import com.example.coupon.dto.MemberResponse
import com.example.coupon.service.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {

    @PostMapping("/signup")
    fun signup(username: String, password: String): MemberResponse = memberService.signup(username, password)

    @PostMapping("/login")
    fun login(username: String, password: String): LoginResponse = memberService.login(username, password)
}