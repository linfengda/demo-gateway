package com.lfd.soa.demo.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heweiming
 * @description
 * @copyright Copyright (C) 2021 PatPat, Inc. All rights reserved. <br>
 * @company 深圳盈富斯科技有限公司
 * @date 2022/3/2
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/healthCheck")
    public Map<String, Object> healthCheck() {
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("data", null);
        return resultMap;
    }
}
