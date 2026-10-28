package com.example.coupon.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    fun handle(e: BusinessException): ProblemDetail = ProblemDetail.forStatusAndDetail(e.status, e.message)

    @ExceptionHandler(Exception::class)
    fun handleOther(e: Exception): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message ?: "서버 오류인 듯")
}
