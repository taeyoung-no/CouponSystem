package com.example.coupon.repository

import com.example.coupon.entity.Coupon
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CouponRepository : JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    fun findByIdForUpdate(@Param("id") id: Long): Coupon?

    @Modifying
    @Query("UPDATE Coupon c SET c.count = c.count - 1 WHERE c.id = :id")
    fun decrementCount(@Param("id") id: Long): Int
}
