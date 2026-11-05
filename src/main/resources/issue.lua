if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
  return -1
end

local count = tonumber(redis.call('GET', KEYS[1])) or 0
if count <= 0 then
  return 0
end

redis.call('DECR', KEYS[1])
redis.call('SADD', KEYS[2], ARGV[1])
redis.call('ZADD', KEYS[3], ARGV[2], ARGV[3])
return 1
