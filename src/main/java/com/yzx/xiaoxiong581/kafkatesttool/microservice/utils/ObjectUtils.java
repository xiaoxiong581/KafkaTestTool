package com.yzx.xiaoxiong581.kafkatesttool.microservice.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.BaseException;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.error.ResultErrorEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author yangzhixiong
 */
@Slf4j
public class ObjectUtils {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String serializeObjectToStr(T source) {
        try {
            return source instanceof String ? (String) source : OBJECT_MAPPER.writeValueAsString(source);
        } catch (Exception e) {
            log.error("serialize object to string error, source: {}" + source, e);
            throw new BaseException(ResultErrorEnum.SERIALIZE_OBJECT_ERROR);
        }
    }

    public static <S, T> T deserializeToObject(S source, Class<T> target) {
        try {
            if (source instanceof InputStream) {
                return OBJECT_MAPPER.readValue((InputStream) source, target);
            }

            if (source instanceof CharSequence) {
                return OBJECT_MAPPER.readValue(String.valueOf(source), target);
            }

            String json = OBJECT_MAPPER.writeValueAsString(source);
            return OBJECT_MAPPER.readValue(json, target);
        } catch (Exception e) {
            log.error("deserialize to object error, source: " + source, e);
            throw new BaseException(ResultErrorEnum.DESERIALIZE_OBJECT_ERROR);
        }
    }
}
