package com.lfd.soa.demo.gateway.util;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>  </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-11 09:34
 */
public class ServerWebExchangeUtil {

    public static Mono<Void> responseChain(ServerWebExchange exchange, String msg, HttpStatus status) {
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
