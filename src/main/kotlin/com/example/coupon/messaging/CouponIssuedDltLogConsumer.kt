package com.example.coupon.messaging

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CouponIssuedDltLogConsumer(
    private val couponIssuedDltLogWriter: CouponIssuedDltLogWriter,
) {

    @KafkaListener(
        topics = [KafkaTopics.COUPON_ISSUED_DLT],
        groupId = "coupon-dlt-log",
        containerFactory = "dltKafkaListenerContainerFactory",
    )
    fun consume(record: ConsumerRecord<String, ByteArray>) = couponIssuedDltLogWriter.write(record)
}
