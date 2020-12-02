package com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.handler;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.BaseResponse;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.BaseException;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.error.ResultErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangzhixiong
 */
@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public BaseResponse handle(final BaseException baseException) {
        return new BaseResponse(baseException.getCode(), baseException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handle(final Exception exception) {
        log.error("catch common exception", exception);
        return new BaseResponse(ResultErrorEnum.SYSTEM_INTERNAL_EXCEPTION.getCode(),
                ResultErrorEnum.SYSTEM_INTERNAL_EXCEPTION.getMessage());
    }
}
