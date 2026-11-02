package com.example.coupon.messaging

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper

@Service
class CouponIssuedDltReplayService(
    private val couponIssuedDltLogRepository: CouponIssuedDltLogRepository,
    private val couponIssuedProducer: CouponIssuedProducer,
) {
    private val jsonMapper = JsonMapper.builder().findAndAddModules().build()

    @Transactional
    fun replay(id: Long) {
        val log = couponIssuedDltLogRepository.findByIdAndStatus(id, CouponIssuedDltStatus.PENDING)
            ?: error("그런 DLT는 없는데요")

        val event = jsonMapper.readValue(log.payload, CouponIssued::class.java)
        couponIssuedProducer.publish(event)
        log.status = CouponIssuedDltStatus.REPLAYED
    }
}
