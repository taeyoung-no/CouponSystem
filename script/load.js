import http from 'k6/http'
import { sleep } from 'k6'

const BASE = 'http://localhost:8080'
const COUPON_COUNT = 1000
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
  const couponRes = http.post(`${BASE}/api/coupon`, { count: COUPON_COUNT })
  const couponId = couponRes.json('id')
  const password = 'qwer1234'

  const tokens = []
  for (let i = 0; i < VU_COUNT; i++) {
    const username = `username${i}`
    http.post(`${BASE}/api/member/signup`, { username, password })
    const loginRes = http.post(`${BASE}/api/member/login`, { username, password })
    tokens.push(loginRes.json('token'))
  }

  return { couponId, tokens }
}

export default function (data) {
  const headers = { Authorization: `Bearer ${data.tokens[__VU - 1]}` }

  let admitted = http.post(`${BASE}/api/waiting-queue/${data.couponId}`, null, { headers }).json('admitted')
  while (admitted === false) {
    sleep(1)
    admitted = http.get(`${BASE}/api/waiting-queue/${data.couponId}`, { headers }).json('admitted')
  }

  http.post(`${BASE}/api/coupon/${data.couponId}/issue`, null, { headers })
}