package com.atguigu.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced //这个注解是因为consul里的服务可能是集群(而服务调用的时候用的是同一个名字),用restTemplate调用的时候也许会报错,需要加上这个注解支持负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
