package com.lfd.soa.demo.gateway.handler;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p> 鉴权接口 </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-08 11:04
 */
public interface WebSessionStrategy {

    /**
     * 验证token
     * @param exchange
     * @param chain
     * @return
     */
    Mono<Void> sessionAuth(ServerWebExchange exchange, GatewayFilterChain chain);

    /**
     * 鉴权类型
     * @return
     */
    String getAuthType();
}
