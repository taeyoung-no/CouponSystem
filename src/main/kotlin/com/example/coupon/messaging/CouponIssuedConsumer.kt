package com.example.coupon.messaging

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CouponIssuedConsumer(private val couponIssuedWriter: CouponIssuedWriter) {

    @KafkaListener(topics = [KafkaTopics.COUPON_ISSUED], concurrency = "3")
    fun consume(event: CouponIssued) {
        try {
            couponIssuedWriter.write(event)
        } catch (_: DataIntegrityViolationException) {
            // 이미 디비에 저장했음
        }
    }
}
