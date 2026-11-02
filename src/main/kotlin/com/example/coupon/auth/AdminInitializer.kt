package com.example.coupon.auth

import com.example.coupon.entity.Member
import com.example.coupon.entity.MemberRole
import com.example.coupon.repository.MemberRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdminInitializer {

    @Bean
    fun init(memberRepository: MemberRepository): ApplicationRunner {
        return ApplicationRunner {
            if (memberRepository.findByUsername("admin") == null) {
                memberRepository.save(Member("admin", "admin", MemberRole.ADMIN))
            }
        }
    }
}
