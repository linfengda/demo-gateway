package com.lfd.soa.demo.gateway.filter;

import com.lfd.soa.demo.gateway.WebSessionStrategyFactory;
import com.lfd.soa.demo.gateway.handler.WebSessionStrategy;
import com.lfd.soa.demo.gateway.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
        String serviceName = headers.getFirst("SERVICE");
        log.info("url = {}, serviceName = {}", uri.getPath(), serviceName);
        if (!StringUtils.isEmpty(serviceName)) {
            WebSessionStrategy strategy = webSessionStrategyFactory.getSessionStrategy(serviceName);
            if (null != strategy) {
                return strategy.sessionAuth(exchange, chain);
            }
        }
        return responseChain(exchange, "非法请求", HttpStatus.UNAUTHORIZED);
    }

    private static Mono<Void> responseChain(ServerWebExchange exchange, String msg, HttpStatus status) {
        Map<String, Object> data = new HashMap<>(8);
        data.put("status", status);
        data.put("msg", msg);
        ServerHttpResponse response = exchange.getResponse();
        byte[] datas = JsonUtil.toJson(data).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(datas);
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
