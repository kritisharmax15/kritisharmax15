package impl;

import base.RateLimiter;

import java.util.HashMap;
import java.util.Map;

public class FixedWindowRateLimiter implements RateLimiter {

    private int maxRequests;
    private long windowSizeInMillis;
    private Map<String, Integer> requestCounts;
    private Map<String, Long> windowStartTimes;

    public FixedWindowRateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestCounts = new HashMap<>();
        this.windowStartTimes = new HashMap<>();
    }

    @Override
    public boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();

        windowStartTimes.putIfAbsent(clientId, currentTime);
        requestCounts.putIfAbsent(clientId, 0);

        long windowStartTime = windowStartTimes.get(clientId);

        // If new time window has started, reset the window start time and reset request counts to zero
        if (currentTime - windowStartTime >= windowSizeInMillis) {
            windowStartTimes.put(clientId, currentTime);
            requestCounts.put(clientId, 0);
        }

        int requestCount = requestCounts.get(clientId);
        if(requestCount < maxRequests) {
            requestCounts.put(clientId, requestCount + 1);
            return true;
        }
        return false;
    }

    @Override
    public void updateConfig(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
    }
}