local members = redis.call('ZRANGE', KEYS[1], 0, tonumber(ARGV[1]) - 1)
if #members == 0 then
  return 0
end

for _, memberId in ipairs(members) do
  redis.call('SET', ARGV[3] .. memberId, '1', 'PX', ARGV[2])
end

redis.call('ZREM', KEYS[1], unpack(members))
return #members
