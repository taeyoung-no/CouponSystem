import http from 'k6/http'
import { check } from 'k6'

export const options = {
  vus: 1,
  iterations: 1,
  thresholds: {
    checks: ['rate==1'],
  },
}

export function setup() {
  const couponRes = http.post('http://localhost:8080/api/coupon', { count: 10 })
  const couponId = couponRes.json('id')

  const username = 'username'
  const password = 'qwer1234'
  http.post('http://localhost:8080/api/member/signup', { username, password })
  const token = http.post('http://localhost:8080/api/member/login', { username, password }).json('token')
  return { couponId, token }
}

export default function (data) {
  const url = `http://localhost:8080/api/coupon/${data.couponId}/issue`
  const params = { headers: { Authorization: `Bearer ${data.token}` } }
  check(http.post(url, null, params), { '1st 200': (r) => r.status === 200 })
  check(http.post(url, null, params), { '2nd 409': (r) => r.status === 409 })
}