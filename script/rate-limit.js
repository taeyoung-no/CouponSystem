import http from 'k6/http'
import { Counter } from 'k6/metrics'

const BASE = 'http://localhost:8090'
const passed = new Counter('passed')
const blocked = new Counter('blocked')

http.setResponseCallback(http.expectedStatuses(200, 429))

export const options = {
  scenarios: {
    abuser: {
      executor: 'constant-arrival-rate',
      rate: 100,
      timeUnit: '1s',
      duration: '10s',
      preAllocatedVUs: 20,
      maxVUs: 100,
    },
  },
}

export function setup() {
  const username = 'username'
  const password = 'qwer1234'
  http.post(`${BASE}/api/member/signup`, { username, password })
  const token = http.post(`${BASE}/api/member/login`, { username, password }).json('token')
  return { token }
}

export default function (data) {
  const res = http.post(`${BASE}/api/waiting-queue/1`, null, {
    headers: { Authorization: `Bearer ${data.token}` },
  })
  if (res.status === 200) {
    passed.add(1)
  }
  if (res.status === 429) {
    blocked.add(1)
  }
}
