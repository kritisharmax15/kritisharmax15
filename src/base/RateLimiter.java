package base;

public interface RateLimiter {
    boolean allowRequest(String clientId);
    void updateConfig(int maxRequests, long windowSizeInMillis);
}
