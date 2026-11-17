package com.example.coupon.service

import com.example.coupon.dto.WaitingQueueResponse
import com.example.coupon.exception.NotInWaitingQueueException
import com.example.coupon.repository.WaitingQueueRepository
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class WaitingQueueService(private val waitingQueueRepository: WaitingQueueRepository) {

    fun enqueue(couponId: Long, memberId: Long): WaitingQueueResponse {
        return toResponse(waitingQueueRepository.enqueue(couponId, memberId))
    }

    fun check(couponId: Long, memberId: Long): WaitingQueueResponse {
        val position = waitingQueueRepository.check(couponId, memberId)
        if (position < 0) {
            throw NotInWaitingQueueException()
        }
        return toResponse(position)
    }

    @Scheduled(fixedRate = 1000)
    @SchedulerLock(name = "waiting-queue-dequeue", lockAtLeastFor = "PT0.95S")
    fun dequeue() {
        waitingQueueRepository.getActiveCouponIds().forEach { couponId ->
            waitingQueueRepository.dequeue(couponId, ADMIT_PER_SECOND, PASS_TTL_MS)
        }
    }

    fun isAdmitted(couponId: Long, memberId: Long): Boolean {
        return waitingQueueRepository.isAdmitted(couponId, memberId)
    }

    private fun toResponse(position: Long): WaitingQueueResponse {
        if (position == 0L) {
            return WaitingQueueResponse(true, 0L)
        }
        return WaitingQueueResponse(false, position)
    }

    private companion object {
        const val ADMIT_PER_SECOND = 100
        const val PASS_TTL_MS = 30_000L
    }
}
