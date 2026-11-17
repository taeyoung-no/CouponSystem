if redis.call('EXISTS', KEYS[2]) == 1 then
  return 0
end

local rank = redis.call('ZRANK', KEYS[1], ARGV[1])
if not rank then
  return -1
end

return rank + 1