package com.yzx.xiaoxiong581.kafkatesttool.microservice.utils;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.BaseResponse;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.error.ResultErrorEnum;

/**
 * @author yangzhixiong
 */
public class ResponseUtils {
    public static BaseResponse newErrorRspByErrorEnum(ResultErrorEnum resultErrorEnum) {
        return new BaseResponse(resultErrorEnum.getCode(), resultErrorEnum.getMessage());
    }
}
