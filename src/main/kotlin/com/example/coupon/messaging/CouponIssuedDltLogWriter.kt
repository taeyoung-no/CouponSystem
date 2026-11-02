package com.example.coupon.messaging

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Component
class CouponIssuedDltLogWriter(
    private val couponIssuedDltLogRepository: CouponIssuedDltLogRepository,
) {

    @Transactional
    fun write(record: ConsumerRecord<String, ByteArray>) {
        val messageKey = "${record.topic()}:${record.partition()}:${record.offset()}"
        if (couponIssuedDltLogRepository.existsByMessageKey(messageKey)) {
            return
        }

        couponIssuedDltLogRepository.save(
            CouponIssuedDltLog(
                messageKey = messageKey,
                payload = record.value().toString(StandardCharsets.UTF_8),
                errorMessage = record.headers()
                    .lastHeader(KafkaHeaders.DLT_EXCEPTION_MESSAGE)
                    ?.value()
                    ?.toString(StandardCharsets.UTF_8),
                receivedAt = LocalDateTime.now(),
            ),
        )
    }
}
