package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rabbitmq;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
public class RabbitMqConfig {
    private String host;

    private int port;

    private String vHost;

    private String usreName;

    private String password;

    private String queueName;
}
