package com.example.coupon.messaging

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CouponIssuedProducer(
    private val kafkaTemplate: KafkaTemplate<String, CouponIssued>,
) {

    fun publish(event: CouponIssued) {
        kafkaTemplate.send(KafkaTopics.COUPON_ISSUED, event.memberId.toString(), event)
    }
}
