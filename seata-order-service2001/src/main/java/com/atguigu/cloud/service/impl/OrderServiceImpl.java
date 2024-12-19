package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StorageFeignApi storageFeignApi;

    @Resource
    private AccountFeignApi accountFeignApi;

    @Override
    @GlobalTransactional(name = "luoqun-creat-order", rollbackFor = Exception.class) //AT模式用的注解,开启seata的全局事务,在入口处开启,加特定名字可以在多个方法上开启全局事务
    public void create(Order order) {
        //xid全局事务id的检查,重要. 方便编码过程中查看xid.
        String xid = RootContext.getXID();
        //1.新建订单
        log.info("------------------新建订单,xid是:" + xid);
        //初始化订单状态为"创建中"
        order.setStatus(0);
        int result = orderMapper.insertSelective(order);
        //判断是否插入成功
        if (result > 0) {
            //这一步主要是为了得到这条记录自动生成的主键信息, 实际开发主键可能是以uuid的形式主动赋值插入?
            Order orderFromDb = orderMapper.selectOne(order);
            log.info("----新建订单成功, 订单: " + orderFromDb.toString());
            System.out.println();
            //2.扣减库存
            log.info("----开始扣减库存");
            storageFeignApi.decrease(orderFromDb.getProductId(), orderFromDb.getCount());
            log.info("----扣减库存完成");
            System.out.println();
            //3.扣减余额
            log.info("----开始扣减余额");
            accountFeignApi.decrease(orderFromDb.getUserId(), orderFromDb.getMoney());
            log.info("----扣减余额完成");
            System.out.println();
            //4.全部流程走完, 订单状态改为"已完成"
            log.info("----修改订单状态");
            orderFromDb.setStatus(1);

            //这一坨其实就是条件查询这条status=0的订单, 改为status=1. 弹幕说直接用mbplus的lambdaUpdate更方便得多
            Example whereCondition=new Example(Order.class);
            Example.Criteria criteria=whereCondition.createCriteria();
            criteria.andEqualTo("userId",orderFromDb.getUserId());
            criteria.andEqualTo("status",0);
            int updateResult = orderMapper.updateByExampleSelective(orderFromDb, whereCondition);

            log.info("----修改订单状态完成");
            log.info("----当前订单状态: " + orderFromDb.getStatus());
            System.out.println();

        }
        log.info("------------------结束新建订单,xid是:" + xid);
    }
}
