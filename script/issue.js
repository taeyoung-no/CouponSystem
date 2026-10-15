import http from 'k6/http'
import { check } from 'k6'

export const options = {
  scenarios: {
    issue: {
      executor: 'per-vu-iterations',
      vus: 50,
      iterations: 1,
    },
  },
}

export function setup() {
  const couponRes = http.post('http://localhost:8080/api/coupon', { count: 10 })
  const couponId = couponRes.json('id')
  const password = 'qwer1234'

  const tokens = []
  for (let i = 0; i < 50; i++) {
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
