import http from 'k6/http'
import crypto from 'k6/crypto'
import encoding from 'k6/encoding'
import exec from 'k6/execution'

const JWT_SECRET = 'jwt-secret-1234567890-very-long-string'

export const options = {
  scenarios: {
    flood: {
      executor: 'constant-arrival-rate',
      rate: 1000,
      timeUnit: '1s',
      duration: '30s',
      preAllocatedVUs: 200,
      maxVUs: 2000,
    },
  },
}

export function setup() {
  const couponId = http.post('http://localhost:8080/api/coupon', { count: 1000000 }).json('id')
  return { couponId }
}

export default function (data) {
  const memberId = exec.scenario.iterationInTest + 1
  http.post(`http://localhost:8080/api/coupon/${data.couponId}/issue`, null, {
    headers: { Authorization: `Bearer ${createToken(memberId)}` },
  })
}

function createToken(memberId) {
  const now = Math.floor(Date.now() / 1000)
  const header = encoding.b64encode(JSON.stringify({ alg: 'HS256', typ: 'JWT' }), 'rawurl')
  const payload = encoding.b64encode(
    JSON.stringify({ sub: String(memberId), role: 'USER', iat: now, exp: now + 3600 }),
    'rawurl',
  )
  const signature = crypto.hmac('sha256', JWT_SECRET, `${header}.${payload}`, 'base64rawurl')
  return `${header}.${payload}.${signature}`
}