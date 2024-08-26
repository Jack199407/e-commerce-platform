package com.ecommerce.order.controller;

import com.ecommerce.infrastructure.repository.mapper.order.OrdersBizMapper;
import com.ecommerce.infrastructure.repository.model.order.Orders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrdersBizMapper ordersBizMapper;

    @PostMapping("/insert/batch")
    public String insertBatch(@RequestBody List<Orders> orders) {
        ordersBizMapper.insertBatch(orders);
        return "success";
    }

    @PostMapping("/queryByMemberIds")
    public List<Orders> queryByMemberIds(@RequestBody List<Long> memberIds) {
        return ordersBizMapper.listByMemberIdOrders(memberIds);
    }
}
