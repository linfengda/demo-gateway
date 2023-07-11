package com.lfd.soa.demo.gateway.limit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * @Author admin
 * @Description 基于Redis的限流工具类
 * @Date 10:15 2021/8/16
 **/
public class SimpleRedisRateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRedisRateLimiter.class);

    private static final String RATE_LIMITER_PREFIX = "com:demo:gateway:rate:limiter:{";
    private static final String RAL_TOKENS_SUFFIX = "}:tokens:";
    private static final String RAL_TIMESTAMP_SUFFIX = "}:timestamp:";
    private static final String RAL_DEFAULT_TOKEN_ACQUIRE = "1";

    private StringRedisTemplate redis;

    private RedisScript<List<Long>> script;

    public SimpleRedisRateLimiter(StringRedisTemplate redis, RedisScript<List<Long>> script) {
        this.redis = redis;
        this.script = script;
    }
    /**
     * @Description 比如1s最多允许10次请求。rate=10，burstCapacity=10
     * @Date 10:26 2021/8/16
     * @Param   rate 速率
     * @Param   capacity 容量
     * @Param   key 业务KEY
     * @return true:允许访问
     **/
    public boolean isAllowed(int rate, int capacity, String key) {
        try {
            String tokenKey = RATE_LIMITER_PREFIX + key + RAL_TOKENS_SUFFIX;
            String timestampKey = RATE_LIMITER_PREFIX + key + RAL_TIMESTAMP_SUFFIX;
            List<String> scriptKeys = Arrays.asList(tokenKey, timestampKey);
            List<Long> ret = redis.execute(
                                            script,
                                            scriptKeys, String.valueOf(rate),
                                            String.valueOf(capacity),
                                            String.valueOf(Instant.now().getEpochSecond()),
                                            RAL_DEFAULT_TOKEN_ACQUIRE);
            return 1 == ret.get(0);
        } catch (Exception ex) {
            /*
             * We don't want a hard dependency on Redis to allow traffic. Make sure to set
             * an alert so you know if this is happening too much. Stripe's observed
             * failure rate is 0.01%.
             */
            logger.error("Error determining if user allowed from redis", ex);
        }
        return true;
    }

}
