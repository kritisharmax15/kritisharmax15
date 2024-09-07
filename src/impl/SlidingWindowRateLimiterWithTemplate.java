package impl;

import base.AbstractRateLimiter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SlidingWindowRateLimiterWithTemplate extends AbstractRateLimiter {

    private Map<String, Queue<Long>> requestTimestamps;

    public SlidingWindowRateLimiterWithTemplate(int maxRequests, long windowSizeInMillis) {
        super(maxRequests, windowSizeInMillis);
        this.requestTimestamps = new HashMap<>();
    }

    @Override
    protected boolean isRequestAllowed(String clientId) {
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
}
