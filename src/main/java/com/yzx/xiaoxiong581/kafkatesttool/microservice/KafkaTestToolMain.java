package com.yzx.xiaoxiong581.kafkatesttool.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yangzhixiong
 */
@EnableScheduling
@ImportResource("classpath:spring/*.xml")
@SpringBootApplication
public class KafkaTestToolMain {
    public static void main(String[] args) {
        SpringApplication.run(KafkaTestToolMain.class, args);
    }
}
