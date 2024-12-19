package com.atguigu.cloud.mygateway;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义的断言工厂类,可以自己拓展写更复杂的断言规则
 * 比如本例中自定义一个会员等级userType,按照银/金/钻和yml配置的会员等级来判断是否可以访问,比如设置只有diamond可以访问
 */
@Component
public class MyRoutePredicateFactory extends AbstractRoutePredicateFactory<MyRoutePredicateFactory.Config> {

    //空参构造方法,但是不明白这里为什么参数是MyRoutePredicateFactory.Config.class而不是类似AfterRoutePredicateFactory里的Config.class
    //后面得到解答了,好像两个是等价的,这里是为了让代码更清晰的指出这Config是本类里的静态内部类
    public MyRoutePredicateFactory() {
        super(MyRoutePredicateFactory.Config.class);
    }

    //加上这个才能支持shortcut格式的yml配置,否则只能用fully expanded的格式
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("userType");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        //匿名内部类,仿照着写的
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                //检查request的参数里面,userType是否是指定的值
                String suerType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");
                if (suerType == null) {
                    return false;
                }
                //如果参数存在,就和config里的值进行比较
                if (suerType.equalsIgnoreCase(config.getUserType())) {
                    return true;
                }
                return false;
            }
        };
    }

    //静态内部类,里面就是断言规则
    @Validated
    public static class Config {

        @Getter
        @Setter
        @NotNull
        private String userType; //银/金/钻和yml配置的会员等级

    }
}
