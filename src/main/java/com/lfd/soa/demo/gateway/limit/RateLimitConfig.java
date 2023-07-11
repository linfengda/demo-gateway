package com.lfd.soa.demo.gateway.limit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

/**
 * @Author mochengqi
 * @Description 基于redis限流配置类
 * @Date 11:13 2021/8/16
 **/
@Configuration
@ConditionalOnClass({ RedisTemplate.class })
public class RateLimitConfig {

    @Bean
    public RedisScript simpleRateLimiterScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(
                new ResourceScriptSource(
                new ClassPathResource("script/simple_request_rate_limiter.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }

    @Bean
    public SimpleRedisRateLimiter simpleRedisRateLimiter(
            @Qualifier("stringRedisTemplate") StringRedisTemplate redis,
            @Qualifier("simpleRateLimiterScript") RedisScript<List<Long>> script) {
        return new SimpleRedisRateLimiter(redis, script);
    }
}
