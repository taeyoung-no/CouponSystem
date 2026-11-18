package com.example.coupon.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/api/hello", "/api/member/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/coupon").hasRole("ADMIN")
                it.requestMatchers("/api/coupon/dlt/**", "/api/coupon/*/reconcile").hasRole("ADMIN")
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                HeaderAuthFilter(),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .build()
    }
}
