package com.ecommerce.member.feign;

import com.ecommerce.infrastructure.repository.model.order.Orders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("ecommerce-order")
public interface OrdersFeignAdapter {

    @RequestMapping("/order/queryByMemberIds")
    List<Orders>  queryOrdersByMemberIds(List<Long> memberIds);

}
