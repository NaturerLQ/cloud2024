package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用来测试 @SentinelResource 注解
 */
@RestController
@Slf4j
public class RateLimitController {
    //sentinel控制台配置流控规则时填写的资源名要么直接填url, 那么会返回Sentinel默认的限流提示: Blocked by Sentinel (flow limiting)
    @GetMapping("/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试OK";
    }

    //--------------------------------------------------
    @GetMapping("/rateLimit/byResource")
    @SentinelResource(value = "byResource001", blockHandler = "blockHandler001")
    //这个value值即byResource001要和sentinel控制台配置流控规则时填写的资源名一致.blockHandler的值要和下面的方法名一致
    public String byResource() {
        return "按Resource名称限流测试OK";
    }

    //方法名要和上面的blockHandler的值一致, 返回值好像也要和上面方法一致?
    public String blockHandler001(BlockException blockException) {
        return "触发了@SentinelResource, 自定义的限流提示: 限流了!!!!!!!!!!!";
    }

    //--------------------------------------------------
    //value值要和sentinel控制台配置流控规则时填写的资源名一致
    //blockHandler和fallback对应的两个方法的参数和返回值要和主方法保持一致(后面加的BlockException和Throwable等参数是固定格式)
    @GetMapping("/rateLimit/doAction/{p1}")
    @SentinelResource(value = "doActionSentinelResource",
            blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0) {
            throw new RuntimeException("p1等于零直接异常");
        }
        return "doAction";
    }

    public String doActionBlockHandler(@PathVariable("p1") Integer p1, BlockException e) {
        log.error("sentinel配置自定义限流了:{}", e);
        return "sentinel配置自定义限流了";
    }

    public String doActionFallback(@PathVariable("p1") Integer p1, Throwable e) {
        log.error("程序逻辑异常了:{}", e);
        return "程序逻辑异常了" + "\t" + e.getMessage();
    }

    /**
     * blockHandler主要针对sentinel里的流控配置,违背了则走blockHandler
     * 而fallback针对程序异常,进行服务降级
     * 两者可以共存
     */

    //-----------------------------------------------------------

    //热点参数限流
    //热点参数指的是参数列表里的p1和p2,比如设置了携带p1参数时qps超过限制就触发限流
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1, //这里的p1,p2对应sentinel控制台配置热点规则时的参数索引(从0开始,即0代表p1,1代表p2,以此类推)
                             @RequestParam(value = "p2", required = false) String p2) {
        return "------testHotKey";
    }

    public String dealHandler_testHotKey(String p1, String p2, BlockException exception) {
        return "-----dealHandler_testHotKey";
    }

    //热点参数限流还可以配置参数例外项,比如携带p1参数的请求正常是qps超过1就限流,但可以设置一个例外项比如p1=999时,携带p1参数的请求qps超过200才限流
    //另外热点参数类型必须是基本类型才能配置这一选项


}
