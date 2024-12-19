package com.atguigu.cloud.mygateway;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 本类的一些细节说明参照MyRoutePredicateFactory里的注释
 */
@Component
public class MyGatewayFilterFactory extends AbstractGatewayFilterFactory<MyGatewayFilterFactory.Config> {

    public MyGatewayFilterFactory(){
        super(MyGatewayFilterFactory.Config.class); //基本都是仿照的某个具体的GatewayFilterFactory写的,具体意义不明
    }

    @Override
    public GatewayFilter apply(MyGatewayFilterFactory.Config config) { //写MyGatewayFilterFactory.Config或者Config都行,两个是等价的,这里是为了让代码更清晰的指出这Config是本类里的静态内部类
        return new GatewayFilter() {
            //其实就是写一个servlet的FilterChain,不过看球不懂,老早接触过一点但是忘干净了
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                System.out.println("进入自定义网关过滤器MyGatewayFilterFactory，status===="+config.getStatus());
                if (request.getQueryParams().containsKey("atguigu")) {
                    return chain.filter(exchange); //放行
                } else {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                }
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("status"); //基本都是仿照的某个具体的GatewayFilterFactory写的,具体意义不明
    }

    public static class Config {
        @Getter
        @Setter
        private String status; //设定一个状态值, 它等于多少时才可以访问
    }
}
