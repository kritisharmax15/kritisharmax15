package util;

import base.RateLimiter;
import resources.RateLimiterType;

public class RateLimiterManager {
    private static RateLimiterManager instance;
    private RateLimiter rateLimiter;

    private RateLimiterManager() {
        // Example initialisation with a fixed window rate limiter
        this.rateLimiter = RateLimiterFactory
                .createRateLimiterWithTemplate(RateLimiterType.FIXED, 5, 60000);
    }

    public static RateLimiterManager getInstance() {
        if (instance == null) {
            synchronized (RateLimiterManager.class) {
                if(instance == null) {
                    instance = new RateLimiterManager();
                }
            }
        }
        return instance;
    }

    public  boolean allowRequest(String clientId) {
        return rateLimiter.allowRequest(clientId);
    }

    public void updateConfig(int maxRequests, long windowSizeInMillis) {
        rateLimiter.updateConfig(maxRequests, windowSizeInMillis);
    }
}
