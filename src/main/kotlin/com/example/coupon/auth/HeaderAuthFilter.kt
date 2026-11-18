package com.example.coupon.auth

import com.example.coupon.entity.MemberRole
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class HeaderAuthFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val memberId = request.getHeader(MEMBER_ID_HEADER)?.toLongOrNull()
        val role = request.getHeader(MEMBER_ROLE_HEADER)
            ?.let { runCatching { MemberRole.valueOf(it) }.getOrNull() }

        if (memberId != null && role != null) {
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                memberId,
                null,
                listOf(SimpleGrantedAuthority("ROLE_${role.name}")),
            )
        }
        filterChain.doFilter(request, response)
    }

    private companion object {
        const val MEMBER_ID_HEADER = "Member-Id"
        const val MEMBER_ROLE_HEADER = "Member-Role"
    }
}