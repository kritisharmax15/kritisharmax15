package util;

import base.RateLimiter;
import config.RateLimiterConfig;
import impl.*;
import resources.RateLimiterType;

public class RateLimiterFactory {

    public static RateLimiter createRateLimiterWithTemplate(RateLimiterType type, int maxRequests, long windowSizeInMillis) {

        switch (type) {
            case RateLimiterType.FIXED :
                return new FixedWindowRateLimiter(maxRequests, windowSizeInMillis);
            case RateLimiterType.SLIDING:
                return new SlidingWindowRateLimiter(maxRequests, windowSizeInMillis);
            default:
                throw new IllegalArgumentException("Unsupported resources.RateLimiterType: " + type);
        }
    }

    public static RateLimiter createRateLimiterWithTemplate(RateLimiterType type, RateLimiterConfig rateLimiterConfig) {
        RateLimiter rateLimiter;

        switch (type) {
            case RateLimiterType.FIXED :
                rateLimiter = new FixedWindowRateLimiterWithTemplate(rateLimiterConfig.getMaxRequests(), rateLimiterConfig.getWindowSizeInMillis());
                break;
            case RateLimiterType.SLIDING:
                rateLimiter = new SlidingWindowRateLimiterWithTemplate(rateLimiterConfig.getMaxRequests(), rateLimiterConfig.getWindowSizeInMillis());
                break;
            case RateLimiterType.REDIS:
                rateLimiter = new RedisRateLimiterWithTemplate(rateLimiterConfig.getMaxRequests(), rateLimiterConfig.getWindowSizeInMillis());
                break;
            default:
                throw new IllegalArgumentException("Unsupported resources.RateLimiterType: " + type);
        }

        rateLimiterConfig.addObserver(rateLimiter);
        return rateLimiter;
    }

}
