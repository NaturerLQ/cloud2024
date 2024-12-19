package com.atguigu.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sentinel授权规则,配置在自定义的接口MyRequestOriginParser中
 */
@RestController
@Slf4j
public class EmpowerController {
    @GetMapping(value = "/empower")
    public String requestSentinel4(){
        log.info("测试Sentinel授权规则empower");
        return "Sentinel授权规则";
    }
}
