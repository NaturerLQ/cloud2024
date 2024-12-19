package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {

    //public static final String PaymentSrv_URL = "http://localhost:8001"; //先写死,硬编码
    public static final String PaymentSrv_URL = "http://cloud-payment-service"; //服务注册中心上的微服务名称

    @Resource
    private RestTemplate restTemplate;

    /**
     * 一般情况下，通过浏览器的地址栏输入url，发送的只能是get请求
     * 我们底层调用的是post方法，模拟消费者发送get请求，客户端消费者
     * 参数可以不添加@RequestBody
     * @param payDTO
     * @return
     */
    @GetMapping(value = "/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO) {
        return restTemplate.postForObject(PaymentSrv_URL + "/pay/add", payDTO, ResultData.class);
    }

    @GetMapping(value = "/consumer/pay/delete/{id}")
    public ResultData deleteOrder(@PathVariable("id") Integer id) {
        restTemplate.delete(PaymentSrv_URL + "/pay/delete/" + id, ResultData.class);
        return ResultData.success("应该删除成功了吧");
    }

    @GetMapping(value = "/consumer/pay/update")
    public ResultData updateOrder(PayDTO payDTO) {
        restTemplate.put(PaymentSrv_URL + "/pay/update", payDTO, ResultData.class);
        return ResultData.success("应该修改成功了吧");
    }

    @GetMapping(value = "/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/" + id, ResultData.class, id);
    }

    //演示负载均衡
    @GetMapping(value = "/consumer/pay/get/info")
    public String getInfoByConsul() {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
    }

    @Resource
    private DiscoveryClient discoveryClient;

    //源码解释负载均衡原理,就是从consul获取所有的服务
    @GetMapping(value = "/consumer/discovery")
    public String discovery(){
        List<String> services = discoveryClient.getServices();
        for (String serviceName : services) {
            System.out.println(serviceName);
        }
        System.out.println("---------------------------");
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getServiceId() + " ; " + instance.getHost() + " ; " + instance.getPort() + " ; " + instance.getUri());
        }
        return instances.get(0).getServiceId() + " ; " +instances.get(0).getPort();
    }




}
