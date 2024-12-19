package com.atguigu.cloud.mygateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {

    private static final String BEGIN_VISIT_TIME = "begin_visit_time"; //开始调用方法的时间
    private static final String END_VISIT_TIME = "end_visit_time"; //结束调用方法的时间

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.先记录访问接口的开始时间
        exchange.getAttributes().put(BEGIN_VISIT_TIME, System.currentTimeMillis());
        //2.返回统计的各个结果给后台
        return chain.filter(exchange).then(Mono.fromRunnable(() -> { //这里的Mono属于响应式编程,前面课程多次看到的Flux也是,需要自己再去拓展
            Long beginVisitTime = exchange.getAttribute(BEGIN_VISIT_TIME);
            if (beginVisitTime != null) { //不等于null表示被调用了
                log.info("访问接口主机: " + exchange.getRequest().getURI().getHost());
                log.info("访问接口端口: " + exchange.getRequest().getURI().getPort());
                log.info("访问接口URL: " + exchange.getRequest().getURI().getPath());
                log.info("访问接口URL参数: " + exchange.getRequest().getURI().getRawQuery());
                log.info("访问接口时长: " + (System.currentTimeMillis() - beginVisitTime) + "ms");
                log.info("我是美丽分割线: ###################################################");
                System.out.println();
            }
                }
        ));
    }

    //数字越小,优先级越大
    @Override
    public int getOrder() {
        return -1;
    }
}
