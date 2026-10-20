import http from 'k6/http'
import { check } from 'k6'

const COUPON_COUNT = 100
const VU_COUNT = 500

export const options = {
  scenarios: {
    issue: {
      executor: 'per-vu-iterations',
      vus: VU_COUNT,
      iterations: 1,
    },
  },
}

export function setup() {
  const couponRes = http.post('http://localhost:8080/api/coupon', { count: COUPON_COUNT })
  const couponId = couponRes.json('id')
  const password = 'qwer1234'

  const tokens = []
  for (let i = 0; i < VU_COUNT; i++) {
    const username = `username${i}`
    http.post('http://localhost:8080/api/member/signup', { username, password })
    const loginRes = http.post('http://localhost:8080/api/member/login', { username, password })
    tokens.push(loginRes.json('token'))
  }

  return { couponId, tokens }
}

export default function (data) {
  const token = data.tokens[__VU - 1]
  http.post(`http://localhost:8080/api/coupon/${data.couponId}/issue`, null, {
    headers: { Authorization: `Bearer ${token}` },
  })
}
