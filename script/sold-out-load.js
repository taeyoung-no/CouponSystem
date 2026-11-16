import http from 'k6/http'

http.setResponseCallback(http.expectedStatuses(200, 409))

export const options = {
  scenarios: {
    sold_out: {
      executor: 'constant-arrival-rate',
      rate: 100,
      timeUnit: '1s',
      duration: '30s',
      preAllocatedVUs: 20,
      maxVUs: 100,
    },
  },
}

export function setup() {
  const couponId = http.post('http://localhost:8080/api/coupon', { count: 0 }).json('id')
  const username = 'username'
  const password = 'qwer1234'
  http.post('http://localhost:8080/api/member/signup', { username, password })
  const token = http.post('http://localhost:8080/api/member/login', { username, password }).json('token')
  return { couponId, token }
}

export default function (data) {
  http.post(`http://localhost:8080/api/coupon/${data.couponId}/issue`, null, {
    headers: { Authorization: `Bearer ${data.token}` },
  })
}