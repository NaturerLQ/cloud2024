package com.atguigu.cloud.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class PayCircuitController {
    //=========Resilience4j CircuitBreaker 的例子
//    @GetMapping(value = "/pay/circuit/{id}")
//    public String myCircuit(@PathVariable("id") Integer id)
//    {
//        if(id < 0) throw new RuntimeException("----circuit id 不能负数");
//        if(id == 9999){
//            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
//        }
//        return "Hello, circuit! inputId:  "+id+" \t " + IdUtil.simpleUUID();
//    }

    //=========Resilience4j bulkhead 的例子
    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id)
    {
        if(id == -4) throw new RuntimeException("----bulkhead id 不能-4");

        if(id == 9999)
        {
            try { TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        //这里有个坑,用浏览器测试并发的时候如果两个url相同(都用id=9999来测试),浏览器在相同url时会默认按照顺序执行不会产生并发从而第三个请求不会降级,导致测试失败
        if(id == 8888)
        {
            try { TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        return "Hello, bulkhead! inputId:  "+id+" \t " + IdUtil.simpleUUID();
    }

    //=========Resilience4j ratelimit 的例子
    @GetMapping(value = "/pay/ratelimit/{id}")
    public String myRatelimit(@PathVariable("id") Integer id)
    {
        return "Hello, myRatelimit欢迎到来 inputId:  "+id+" \t " + IdUtil.simpleUUID();
    }
}
