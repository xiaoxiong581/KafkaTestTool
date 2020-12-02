package com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应异常枚举类
 * <p>
 * 业务异常：10000~19999
 * 公共异常：100~999
 *
 * @author yangzhixiong
 */
@AllArgsConstructor
public enum ResultErrorEnum {
    SERIALIZE_OBJECT_ERROR("100", "serialize object to string error"),

    DESERIALIZE_OBJECT_ERROR("101", "deserialize to object error"),

    SYSTEM_INTERNAL_EXCEPTION("102", "system internal exception"),

    PARAM_VALID_ERROR("103", "param valid error");

    @Getter
    private String code;

    @Getter
    private String message;
}
