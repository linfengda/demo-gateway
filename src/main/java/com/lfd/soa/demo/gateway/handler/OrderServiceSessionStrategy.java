package com.lfd.soa.demo.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p> order-service鉴权 </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-08 11:05
 */
@Slf4j
@RefreshScope
@Component
public class OrderServiceSessionStrategy implements WebSessionStrategy {

    @Override
    public Mono<Void> sessionAuth(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("--------------order-service鉴权");
        return chain.filter(exchange);
    }

    @Override
    public String getAuthType() {
        return "order";
    }
}
