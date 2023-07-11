package com.lfd.soa.demo.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> url限流配置 </p>
 *
 * @author linfengda
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2023-07-11 10:09
 */
@Data
@ConfigurationProperties("gateway.rate.limit")
@Configuration
@RefreshScope
public class RateLimitProperties {
    /**
     * 以list的形式接收配置信息
     */
    private List<Map<String, Object>> list = new ArrayList<>();
}
