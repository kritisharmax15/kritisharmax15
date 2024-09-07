import base.RateLimiter;
import config.RateLimiterConfig;
import resources.RateLimiterType;
import util.RateLimiterFactory;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        test1();
        // test2();

    }

    private static void test2() {
        RateLimiter redisRateLimiter = RateLimiterFactory
                .createRateLimiterWithTemplate(RateLimiterType.REDIS, new RateLimiterConfig(5, 30000));

        for(int i=1; i<=7; i++) {
            System.out.println("Client1 - " + i + " - " + redisRateLimiter.allowRequest("Client1"));
            System.out.println("Client2 - " + i + " - " + redisRateLimiter.allowRequest("Client2"));
        }
    }

    private static void test1() throws InterruptedException {
        RateLimiter fixedWindowRateLimiter = RateLimiterFactory
                .createRateLimiterWithTemplate(RateLimiterType.FIXED, 5, 60000);
        RateLimiter slidingWindowRateLimiter = RateLimiterFactory
                .createRateLimiterWithTemplate(RateLimiterType.SLIDING, 5, 60000);

        Runnable client1 =() -> {
            for(int i=0; i<7; i++) {
                System.out.println("Client1 - " + i + " -" + fixedWindowRateLimiter.allowRequest("Client1"));
                System.out.println("Client1 - " + i + " - " + slidingWindowRateLimiter.allowRequest("Client1"));
                if(i == 4) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        Runnable client2 = () -> {
            for(int i=0; i<7; i++) {
                //System.out.println("Client2 - " + fixedWindowRateLimiter.allowRequest("Client2"));
                System.out.println("Client2 - " + i + " - " + slidingWindowRateLimiter.allowRequest("Client2"));
            }
        };

        Thread t1 = new Thread(client1);
        Thread t2 = new Thread(client2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Now updating config for rate limiter - ");
        slidingWindowRateLimiter.updateConfig(2,60000);
        Thread t3 = new Thread(client1);
        t3.start();
        t3.join();
    }
}
