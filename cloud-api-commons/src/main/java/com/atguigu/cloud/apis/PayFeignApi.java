package com.atguigu.cloud.apis;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(value = "cloud-payment-service")
@FeignClient(value = "cloud-gateway")
public interface PayFeignApi {

    @PostMapping(value = "/pay/add")
    public ResultData<String> addPay(@RequestBody PayDTO payDTO);

    //这个先注释了,和后面测试gateway方法同名了
//    @GetMapping(value = "/pay/get/{id}")
//    public ResultData<PayDTO> getById(@PathVariable("id") Integer id);

    //验证openFeign天然支持负载均衡
    @GetMapping(value = "/pay/get/info")
    public String getConsulInfo();

    //Resilience4j CircuitBreaker 的例子
    @GetMapping(value = "/pay/circuit/{id}")
    public String myCircuit(@PathVariable("id") Integer id);

    //Resilience4j Bulkhead 的例子
    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id);

    //Resilience4j Ratelimit 的例子
    @GetMapping(value = "/pay/ratelimit/{id}")
    public String myRatelimit(@PathVariable("id") Integer id);

    //Micrometer(Sleuth)进行链路监控的例子
    @GetMapping(value = "/pay/micrometer/{id}")
    public String myMicrometer(@PathVariable("id") Integer id);

    //GateWay进行网关测试案例01
    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData getById(@PathVariable("id") Integer id);

    //GateWay进行网关测试案例02
    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo();
}
