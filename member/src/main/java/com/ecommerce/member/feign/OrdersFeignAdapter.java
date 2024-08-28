package com.ecommerce.member.feign;

import com.ecommerce.infrastructure.repository.model.order.Orders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "ECOMMERCE-ORDER", path = "/order")
public interface OrdersFeignAdapter {

    @PostMapping("/queryByMemberIds")
    List<Orders>  queryOrdersByMemberIds(List<Long> memberIds);

}
