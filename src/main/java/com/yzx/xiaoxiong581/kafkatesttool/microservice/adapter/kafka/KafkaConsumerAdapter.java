package com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.kafka;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka.ConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author yangzhixiong
 */
@Slf4j
public class KafkaConsumerAdapter extends Thread {
    private KafkaConsumer<String, String> consumer;

    private ConsumerConfig consumerConfig;

    private String taskName;

    public KafkaConsumerAdapter(String taskName, ConsumerConfig consumerConfig) {
        this.taskName = taskName;
        this.consumerConfig = consumerConfig;
        Properties props = new Properties();
        props.put("bootstrap.servers", consumerConfig.getBrokerUrl());
        props.put("group.id", consumerConfig.getConsumerGroupId());
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(consumerConfig.getTopicName()));
        log.info("{} init kafka consumer, bootstrapServer: {}", taskName, consumerConfig.getBrokerUrl());
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("{} receive, topic: {}, key: {}, value: {}, partition: {}, offset: {}, pollSize: {}",
                            taskName,
                            consumerConfig.getTopicName(),
                            record.key(), record.value(), record.partition(), record.offset(), records.count());
                }

                // 用于任务线程停止
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            log.info("taskName {} consumer receive stop signal, stop task", taskName);
        } catch (Exception e) {
            log.error("{} catch exception when r, exception: {}", taskName, e.getMessage());
        }
        consumer.close();
    }
}
