package com.yzx.xiaoxiong581.kafkatesttool.microservice.controller.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangzhixiong
 */
@Slf4j
@RestController
public class HealthCheckController {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "healthcheck", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String healthCheck() {
        return "success";
    }
}
