package impl;

import base.AbstractRateLimiter;
import repository.RedisRepository;

public class RedisRateLimiterWithTemplate extends AbstractRateLimiter {

    private RedisRepository redisRepository;

    public RedisRateLimiterWithTemplate(int maxRequests, long windowSizeInMillis) {
        super(maxRequests, windowSizeInMillis);
        this.redisRepository = RedisRepository.getRedisRepository();
    }

    @Override
    protected boolean isRequestAllowed(String clientId) {
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

}
