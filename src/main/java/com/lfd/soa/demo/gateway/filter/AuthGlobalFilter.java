package com.lfd.soa.demo.gateway.filter;

import com.lfd.soa.demo.gateway.WebSessionStrategyFactory;
import com.lfd.soa.demo.gateway.handler.WebSessionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;

/**
 * <p> 全局过滤器 </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-08 10:58
 */
@Slf4j
@Component
@Order(-20)
public class AuthGlobalFilter implements GlobalFilter {
    @Resource
    private WebSessionStrategyFactory webSessionStrategyFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String service = headers.getFirst("SERVICE");
        log.info("url = {}, service = {}", uri.getPath(), service);
        if (!StringUtils.isEmpty(service)) {
            WebSessionStrategy strategy = webSessionStrategyFactory.getSessionStrategy(service);
            if (null != strategy) {
                return strategy.sessionAuth(exchange, chain);
            }
        }
        return chain.filter(exchange);
        //return ServerWebExchangeUtil.responseChain(exchange, "非法请求", HttpStatus.UNAUTHORIZED);
    }
}
