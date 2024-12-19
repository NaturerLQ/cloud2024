package com.atguigu.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 用于Sentinel中的授权相关的设置
 * 比如在sentinel控制台配置授权规则中的流控应用项test1,test2.....为黑名单
 * 这里再配置serverName,当请请求中带参数serverName=test1/test2就会触发Sentinel的限流
 */
@Component
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        return request.getParameter("serverName");
    }
}
