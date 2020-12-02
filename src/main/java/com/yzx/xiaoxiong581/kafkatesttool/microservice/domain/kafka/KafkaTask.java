package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
public class KafkaTask {
    private String taskName;

    private ConsumerConfig consumerConfig;

    private ProducerConfig producerConfig;
}
