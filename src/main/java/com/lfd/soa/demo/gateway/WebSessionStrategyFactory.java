package com.lfd.soa.demo.gateway;

import com.lfd.soa.demo.gateway.handler.WebSessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p> 根据服务名获取鉴权策略 </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-08 11:03
 */
@Component
public class WebSessionStrategyFactory {
    private Map<String, WebSessionStrategy> sessionStrategyMap = new HashMap<>();

    /**
     * 使用构造器注入WebSessionStrategy实现
     * @param strategies
     */
    public WebSessionStrategyFactory(List<WebSessionStrategy> strategies) {
        if (!CollectionUtils.isEmpty(strategies)) {
            Map<String, WebSessionStrategy> strategyMap = strategies.stream().collect(Collectors.toMap(WebSessionStrategy::getAuthType, Function.identity(), (k1, k2) -> k2));
            sessionStrategyMap.putAll(strategyMap);
        }
    }

    public WebSessionStrategy getSessionStrategy(String serviceName){
        if (StringUtils.isEmpty(serviceName)) {
            return null;
        }
        return sessionStrategyMap.get(serviceName);
    }
}
