package repository;

import redis.clients.jedis.Jedis;

public class RedisRepository {

    private static RedisRepository redisRepository;
    private Jedis jedis;

    public static RedisRepository getRedisRepository() {
        if(redisRepository == null) {
            synchronized (RedisRepository.class) {
                if (redisRepository == null) {
                    redisRepository = new RedisRepository();
                }
            }
        }
        return redisRepository;
    }

    private RedisRepository() {
        this.jedis = new Jedis("127.0.0.1", 6379);
    }

    public void setRequestCount(String key, int requestCount) {
        jedis.hset(key, "requestCount", String.valueOf(requestCount));
    }

    public void setLastRequestTime(String key, long currentTime) {
        jedis.hset(key, "lastRequestTime", String.valueOf(currentTime));
    }

    public long getLastRequestTime(String key) {
        String lastRequestTimeStr = jedis.hget(key, "lastRequestTime");
        return lastRequestTimeStr == null? 0 : Long.valueOf(lastRequestTimeStr);
    }

    public int getRequestCount(String key) {
        String requestCountStr = jedis.hget(key, "requestCount");
        return requestCountStr == null? 0: Integer.valueOf(requestCountStr);
    }
}
