package com.example.coupon.exception

import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handle(e: BusinessException): ProblemDetail = ProblemDetail.forStatusAndDetail(e.status, e.message)
}
