package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.rocketmq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class ProducerConfig extends RocketMqConfig {
    private int msgLength;

    private int msgCount;

    private int sendTimeInterval;
}
