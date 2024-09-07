package config;

import base.RateLimiter;

import java.util.ArrayList;
import java.util.List;

public class RateLimiterConfig {
    private int maxRequests;
    private long windowSizeInMillis;
    List<RateLimiter> observers;

    public RateLimiterConfig(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
        this.observers = new ArrayList<>();
    }

    public void addObserver(RateLimiter rateLimiter) {
        observers.add(rateLimiter);
    }

    public void removeObserver(RateLimiter rateLimiter) {
        observers.remove(rateLimiter);
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
        notifyObservers();
    }

    public void setWindowSizeInMillis(long windowSizeInMillis) {
        this.windowSizeInMillis = windowSizeInMillis;
        notifyObservers();
    }

    private void notifyObservers() {
        for(RateLimiter rateLimiter : this.observers)
            rateLimiter.updateConfig(this.maxRequests, this.windowSizeInMillis);
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeInMillis() {
        return windowSizeInMillis;
    }
}
