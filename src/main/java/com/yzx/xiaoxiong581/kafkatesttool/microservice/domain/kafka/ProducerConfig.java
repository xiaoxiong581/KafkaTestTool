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
public class ProducerConfig extends KafkaConfig {
    private int msgLength;

    private int msgCount;

    private int sendTimeIntervalMs;
}
