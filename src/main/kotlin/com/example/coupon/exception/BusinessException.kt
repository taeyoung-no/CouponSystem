package com.example.coupon.exception

import org.springframework.http.HttpStatus

sealed class BusinessException(
    val status: HttpStatus,
    message: String,
) : RuntimeException(message)

class CouponNotFoundException : BusinessException(HttpStatus.NOT_FOUND, "없는 쿠폰임")

class AlreadyIssuedException : BusinessException(HttpStatus.CONFLICT, "이미 발급한 거임")

class SoldOutException : BusinessException(HttpStatus.CONFLICT, "다 팔렸음")

class UnauthorizedException : BusinessException(HttpStatus.UNAUTHORIZED, "뭔가 잘못 입력함")

class NotInWaitingQueueException : BusinessException(HttpStatus.NOT_FOUND, "대기열에 없음")

class ShouldWaitException: BusinessException(HttpStatus.FORBIDDEN, "기다리셈")
