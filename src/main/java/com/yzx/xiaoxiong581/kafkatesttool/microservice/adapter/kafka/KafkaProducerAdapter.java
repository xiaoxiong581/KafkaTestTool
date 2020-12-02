package com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.kafka;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka.ProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

/**
 * @author yangzhixiong
 */
@Slf4j
public class KafkaProducerAdapter extends Thread {
    private KafkaProducer<String, String> producer;

    private ProducerConfig producerConfig;

    private String taskName;

    public KafkaProducerAdapter(String taskName, ProducerConfig producerConfig) {
        this.taskName = taskName;
        this.producerConfig = producerConfig;
        Properties props = new Properties();
        props.put("bootstrap.servers", producerConfig.getBrokerUrl());
        props.put("acks", "1");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
        log.info("{} init kafka producer, bootstrapServer: {}", taskName, producerConfig.getBrokerUrl());
    }

    @Override
    public void run() {
        int sendMessageNum = producerConfig.getMsgCount();
        int valueLength = producerConfig.getMsgLength();

        String value = RandomStringUtils.randomAlphanumeric(valueLength);
        try {
            if (0 == sendMessageNum) {
                while (!Thread.currentThread().isInterrupted()) {
                    sendMessage(value);
                }
            }

            for (int i = 0; i < sendMessageNum; i++) {
                sendMessage(value);
            }
        } catch (InterruptedException e) {
            log.info("taskName {} producer receive stop signal, stop task", taskName);
        }
        producer.close();
    }

    private void sendMessage(String value) throws InterruptedException {
        String key = UUID.randomUUID().toString();

        try {
            producer.send(new ProducerRecord<>(producerConfig.getTopicName(), key, value));
            log.info("{} send, topic: {}, key: {}, value: {}",
                    taskName, producerConfig.getTopicName(), key, value);
            if (producerConfig.getSendTimeIntervalMs() > 0) {
                Thread.sleep(producerConfig.getSendTimeIntervalMs());
            } else {
                // 用于任务线程停止
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            log.error("{} catch exception when s, topic: {}, key: {}, value: {}, exception: {}", taskName, producerConfig.getTopicName(), key, value,
                    e.getMessage());
        }
    }
}
