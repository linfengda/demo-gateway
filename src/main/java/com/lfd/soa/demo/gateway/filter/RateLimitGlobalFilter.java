package com.lfd.soa.demo.gateway.filter;

import com.lfd.soa.demo.gateway.config.RateLimitProperties;
import com.lfd.soa.demo.gateway.limit.SimpleRedisRateLimiter;
import com.lfd.soa.demo.gateway.util.ServerWebExchangeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * <p> 全局请求限流过滤器 </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-11 09:33
 */
@Slf4j
@Component
public class RateLimitGlobalFilter implements GlobalFilter {
    @Resource
    private SimpleRedisRateLimiter simpleRedisRateLimiter;
    @Resource
    private RateLimitProperties rateLimitProperties;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        String path = uri.getPath();
        // 根据配置决定是否限流
        List<Map<String, Object>> list = rateLimitProperties.getList();
        if (!CollectionUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                if (path.equals(map.get("url"))) {
                    int burstCapacity = (int) map.get("burstCapacity");
                    int replenishRate = (int) map.get("replenishRate");
                    boolean allowed = simpleRedisRateLimiter.isAllowed(burstCapacity, replenishRate, path);
                    if (!allowed) {
                        return ServerWebExchangeUtil.responseChain(exchange, "请求被限流了", HttpStatus.OK);
                    }
                }
            }
        }
        return chain.filter(exchange);
    }
}
