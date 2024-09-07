package impl;

import base.RateLimiter;
import repository.RedisRepository;

public class RedisRateLimiter implements RateLimiter {
    private int maxRequests;
    private long windowSizeInMillis;
    private RedisRepository redisRepository;

    public RedisRateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
        this.redisRepository = RedisRepository.getRedisRepository();
    }

    @Override
    public boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();

        String key = "rate_limiter:"+clientId;
        long lastRequestTime = redisRepository.getLastRequestTime(key);
        int requestCount = redisRepository.getRequestCount(key);

        if(currentTime - lastRequestTime >= windowSizeInMillis) {
            requestCount = 0;
            lastRequestTime = currentTime;
        }

        if(requestCount < maxRequests) {
            redisRepository.setLastRequestTime(key, currentTime);
            redisRepository.setRequestCount(key, requestCount+1) ;
            return true;
        }
        return false;
    }

    @Override
    public void updateConfig(int maxRequests, long windowSizeInMillis) {

    }
}
