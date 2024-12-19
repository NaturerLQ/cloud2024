package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping(value = "/pay/add")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return ResultData.success("成功插入记录, 返回值: " + i);
    }

    @DeleteMapping(value = "/pay/delete/{id}")
    public ResultData<Integer> deletePay(@PathVariable("id") Integer id){
        int i = payService.delete(id);
        return ResultData.success(i);
    }

    @PutMapping(value = "/pay/update")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
        Pay pay = new Pay();
        BeanUtils.copyProperties(payDTO, pay);
        int i = payService.update(pay);
        return ResultData.success("成功修改记录, 返回值: " + i);
    }

    @GetMapping(value = "/pay/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id")Integer id) {
        if(id <= 0) {
            throw new RuntimeException("id不能为负数");
        }
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping(value = "/pay/getAll")
    public ResultData<List<Pay>> getAll(){
        List<Pay> all = payService.getAll();
        return ResultData.success(all);
    }

    @Value("${server.port}") //注意包导org的
    private String port;

    @GetMapping(value = "/pay/get/info")
    public String getConsulInfo(@Value("${atguigu.info}") String info) {
        return "从consul配置里读取的信息是: " + info + "\t" + "读取的端口号是: " + port;
    }
}
