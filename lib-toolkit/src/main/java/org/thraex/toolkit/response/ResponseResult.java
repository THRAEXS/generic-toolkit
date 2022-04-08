package org.thraex.toolkit.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * TODO: Opt
 *
 * @author 鬼王
 * @date 2022/03/10 10:52
 */
public class ResponseResult<T> implements Serializable {

    private static final String STATUS_MUST_NOT_BE_NULL = "status must not be null!";

    private Integer code;

    private T data;

    private String message;

    public ResponseResult() {}

    public ResponseResult(ResponseStatus status, T data, String message) {
        Assert.notNull(status, STATUS_MUST_NOT_BE_NULL);

        this.code = status.value();
        this.data = data;
        this.message = message;
    }

    public static ResponseResult of(ResponseStatus status) {
        return of(status, null, null);
    }

    public static <T> ResponseResult<T> of(ResponseStatus status, T data) {
        return of(status, data, null);
    }

    public static <T> ResponseResult<T> of(ResponseStatus status, T data, String message) {
        return new ResponseResult<>(status, data, message);
    }

    public static ResponseResult ok() {
        return of(ResponseStatus.OK);
    }

    public static <T> ResponseResult<T> ok(T data) {
        return ok().setData(data);
    }

    public static <T> ResponseResult<T> ok(Optional<T> data) {
        // TODO: Opt generic type
        return ok().setData(data);
    }

    public static <T> ResponseResult<PageWrapper<T>> ok(Page<T> page) {
        Assert.notNull(page, "page must not be null.");

        int pages = page.getTotalPages();
        long elements = page.getTotalElements();
        int number = page.getNumber();
        int size = page.getSize();
        List<T> content = page.getContent();

        return ok(new PageWrapper(pages, elements, number, size, content));
    }

    public static ResponseResult fail() {
        ResponseStatus status = ResponseStatus.INTERNAL_SERVER_ERROR;
        return of(status, null, status.phrase());
    }

    public static ResponseResult fail(ResponseStatus status) {
        Assert.notNull(status, STATUS_MUST_NOT_BE_NULL);
        return fail(status, status.phrase());
    }

    public static ResponseResult fail(String message) {
        return fail(ResponseStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ResponseResult fail(ResponseStatus status, String message) {
        return of(status, null, message);
    }

    public Integer getCode() {
        return code;
    }

    public ResponseResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
