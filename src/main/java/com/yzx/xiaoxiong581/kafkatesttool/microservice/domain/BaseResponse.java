package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
public class BaseResponse {
    private String code = "0";

    private String message = "success";

    private Object data;

    public BaseResponse(Object data) {
        this.data = data;
    }

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
