package com.example.gateway

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class RateLimitConfig {

    @Bean
    fun memberIdKeyResolver(): KeyResolver {
        return KeyResolver { exchange ->
            val memberId = exchange.request.headers.getFirst(AuthenticationFilter.MEMBER_ID_HEADER)
            if (memberId == null) {
                Mono.empty()
            } else {
                Mono.just("member:$memberId")
            }
        }
    }
}
