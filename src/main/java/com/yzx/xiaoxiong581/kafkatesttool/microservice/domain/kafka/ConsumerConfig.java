package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class ConsumerConfig extends KafkaConfig {
    private String consumerGroupId;
}
