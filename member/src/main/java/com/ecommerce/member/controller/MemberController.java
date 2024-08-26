package com.ecommerce.member.controller;

import com.ecommerce.infrastructure.repository.model.order.Orders;
import com.ecommerce.member.feign.OrdersFeignAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private OrdersFeignAdapter ordersFeignAdapter;
    @PostMapping("/query/orders")
    public List<Orders> queryByMemberIds(@RequestBody List<Long> memberIds) {
        return ordersFeignAdapter.queryOrdersByMemberIds(memberIds);
    }

}
