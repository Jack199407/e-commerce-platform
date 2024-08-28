package com.ecommerce.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Pager {

    /**
     * 每页记录数量
     */
    private Integer pageSize;
    /**
     * 当前页码
     */
    private Integer current;
    /**
     * 总数
     */
    private Integer total;

    /**
     * 获取偏移量
     */
    public int getOffset() {
        return this.current != null && this.pageSize != null ? (this.current - 1) * this.pageSize
                                                             : 0;
    }
}