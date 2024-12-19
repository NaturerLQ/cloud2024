package com.atguigu.cloud.controller;

import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.apis.PayFeignApi;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {

    @Resource
    private PayFeignApi payFeignApi;

    @PostMapping("/feign/pay/add")
    public ResultData<String> addPay(@RequestBody PayDTO payDTO) {
        ResultData<String> resultData = payFeignApi.addPay(payDTO);
        return resultData;
    }

    @GetMapping("/feign/pay/get/{id}")
    public ResultData<PayDTO> getPayInfo(@PathVariable("id") Integer id) {
        ResultData<PayDTO> resultData = null;
        try{
            System.out.println("调用开始: " + DateUtil.now()); //测试openfeign调用的默认超时时间,以及测试配置自定义时间
            resultData = payFeignApi.getById(id);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用结束: " + DateUtil.now());
            return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
        }

        return resultData;
    }

    //验证openFeign天然支持负载均衡
    @GetMapping("/feign/pay/get/info")
    public String getConsulInfo(){
        String info = payFeignApi.getConsulInfo();
        System.out.println(info);
        return info;
    }
}
