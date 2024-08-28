package com.ecommerce.infrastructure.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 前后端交互返回结果
 *
 * @author qiupeng
 * @date 2020/12/15
 **/
@ToString
@NoArgsConstructor
public class RestResponse<T> implements Serializable {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final String SERVER_NAME = "E-COMMERCE-PLATFORM";

    /**
     * default success response
     */
    public static final RestResponse<Void> RES_SUCCESS = success(null);
    /**
     *
     */
    private static final long serialVersionUID = 605544666575940834L;
    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private T data;
    @Getter
    @Setter
    private String msg;
    @Getter
    @Setter
    private Pager pager;

    @Getter
    @Setter
    private String source;

    private RestResponse(final int code, final T result, final String msg,
                         final Pager pager) {
        this.data = result;
        this.pager = pager;
        this.code = code;
        this.msg = msg;
        this.source = SERVER_NAME;
    }

    private RestResponse(final int code, final T result, final String msg,
                         final Pager pager, final int errorTypeCode) {
        this.data = result;
        this.pager = pager;
        this.code = code;
        this.msg = msg;
        this.source = SERVER_NAME;
    }

    public static <T> RestResponse<T> success(final T result) {
        return new RestResponse<>(SUCCESS, result, null, null);
    }

    public static <T> RestResponse<T> success(final int Scode, final T result, final String mes,
            final Pager p) {
        return new RestResponse<>(Scode, result, mes, p);
    }

    public static <T> RestResponse<T> success(final T result, final Pager pager) {
        return new RestResponse<>(SUCCESS, result, null, pager);
    }

    public static <T> RestResponse<T> fail(final T result, final String msg,
            final Pager pager) {
        return new RestResponse<>(FAIL, result, msg, pager);
    }

    public static <T> RestResponse<T> fail(final String msg, final Pager pager) {
        return new RestResponse<>(FAIL, null, msg, pager);
    }

    public static <T> RestResponse<T> fail(final String msg) {
        return new RestResponse<>(FAIL, null, msg, null);
    }
}
