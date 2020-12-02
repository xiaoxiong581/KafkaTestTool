package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rocketmq;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
public class RocketMqConfig {
    private String url;

    private String groupName;

    private String topic;
}
