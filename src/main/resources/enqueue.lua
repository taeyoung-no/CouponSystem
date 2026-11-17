if redis.call('EXISTS', KEYS[2]) == 1 then
  return 0
end

local t = redis.call('TIME')
local score = tonumber(t[1]) * 1000000 + tonumber(t[2])
redis.call('ZADD', KEYS[1], 'NX', score, ARGV[1])

return redis.call('ZRANK', KEYS[1], ARGV[1]) + 1
