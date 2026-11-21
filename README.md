# Coupon System

트래픽 급증을 버티는 학습용 쿠폰 발급 시스템 프로젝트입니다.

## 목차
1. [개발 노트](#개발-노트)
2. [기술 스택](#기술-스택)
3. [로컬 실행](#로컬-실행)

## 개발 노트
[쿠폰 발급 시스템 동시성 제어와 응답 지연 개선](https://taeyoung-no.github.io/2026/11/18/coupon-post.html)
- 쿠폰 100장에 500명이 동시에 요청하면 500장이 발급되는 race condition을 비관적 락으로 해결
- 실패할 요청도 DB 락을 기다리는 문제를 Redis Lua 선차감으로 해결, p95 2.6s에서 1.02s로 감소
- Redis에서 발급이 확정돼도 DB 쓰기를 기다리는 문제를 Kafka 비동기 저장으로 해결, p95 1.02s에서 349.27ms로 감소

[쿠폰 발급 시스템 트래픽 제어하기](https://taeyoung-no.github.io/2026/11/19/coupon-traffic.html)
- 매진 후에도 모든 요청이 Redis에 접근하는 문제를 L1/L2 캐시로 해결, 30초간 Redis 접근 3,000번에서 30번으로 감소
- DB 처리량(105TPS)보다 많은 요청이 앱 서버에 들어오는 문제를 대기열로 해결, 초당 100개만 입장
- 서버가 두 대면 입장 속도가 두 배가 되는 문제를 분산 락으로 해결
- 한 사용자의 과도한 요청을 Spring Cloud Gateway의 회원별 Rate Limit으로 차단

[쿠폰 발급 시스템 정합성 지키기](https://taeyoung-no.github.io/2026/11/21/coupon-consistency.html)
- Redis에서 발급이 확정됐는데 데이터베이스 쓰기에 실패하는 문제를 DLT 재처리로 해결
- Redis 발급자 목록 유실로 쿠폰이 중복 발급되는 문제를 Reconcile로 해결
- 재고 불일치는 어느 쪽이 틀렸는지 알 수 없어 자동 보정 대신 감지만 구현

## 기술 스택
핵심이라고 생각하는 것만 정리했습니다.

| 영역 | 기술 |
|------|------|
| 백엔드 | Spring Boot, Kotlin |
| API 게이트웨이 | Spring Cloud Gateway |
| DB | MySQL |
| 캐시 | Redis |
| 메시지 큐 | Kafka |
| 부하 테스트 | k6 |
| 인프라 | Docker |

## 로컬 실행
### 요구사항
- Java 21
- Docker, Docker Compose
- k6 (부하 테스트 시)

### 인프라
```bash
docker compose up -d
```

### 개발 서버
```bash
# 앱 서버 (8080)
./gradlew :bootRun
```

```bash
# 게이트웨이 (8090)
./gradlew :gateway:bootRun
```
