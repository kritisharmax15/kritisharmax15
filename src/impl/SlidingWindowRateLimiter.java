package impl;

import base.RateLimiter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SlidingWindowRateLimiter implements RateLimiter {

    private int maxRequests;
    private long windowSizeInMillis;
    private Map<String, Queue<Long>> requestTimestamps;

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestTimestamps = new HashMap<>();
    }

    @Override
    public boolean allowRequest(String clientId) {
        long currentTime = System.currentTimeMillis();
        requestTimestamps.putIfAbsent(clientId, new LinkedList<>());

        Queue<Long> timestamps = requestTimestamps.get(clientId);
        while(!timestamps.isEmpty() && currentTime - timestamps.peek() > windowSizeInMillis) {
            timestamps.poll();
        }

        // If there is space in queue to hold current request, allow it
        if(timestamps.size() < maxRequests) {
            timestamps.add(currentTime);
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
