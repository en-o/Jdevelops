--[[
 lua 脚本进行延迟消费
 @see https://blog.csdn.net/Dongguabai/article/details/114001220
--]]
local key = KEYS[1]
local min = ARGV[1]
local max = ARGV[2]
local result = redis.call('zrangebyscore',key,min,max,'LIMIT',0,10)
if next(result) ~= nil and #result > 0 then
    local re = redis.call('zrem',key,unpack(result));
    if re > 0 then
        return result;
    end
else
    return {}
end

