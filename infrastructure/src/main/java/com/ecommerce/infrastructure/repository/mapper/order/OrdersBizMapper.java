package com.ecommerce.infrastructure.repository.mapper.order;

import com.ecommerce.infrastructure.repository.model.order.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersBizMapper {
    void insertBatch(@Param("records") List<Orders> records);

    List<Orders> listByMemberIdOrders(@Param("memberIds") List<Long> memberIds);
}