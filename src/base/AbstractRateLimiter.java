package base;

public abstract class AbstractRateLimiter implements RateLimiter {
    protected int maxRequests;
    protected long windowSizeInMillis;

    public AbstractRateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override
    public boolean allowRequest(String clientId) {
        return isRequestAllowed(clientId);
    }

    @Override
    public void updateConfig(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    protected abstract boolean isRequestAllowed(String clientId);

}

