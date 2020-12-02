# KafkaTestTool

```sh
创建kafka消费和生产任务（也支持二选一）
curl http://127.0.0.1:29080/kafkatesttool/kafka/task/create -X POST -H "Content-Type:application/json" -d '{"taskName":"task-bignum","consumerConfig":{"brokerUrl":"kf3efb60-bd08-4543-8cd8-b79fa17aadea-kafka-bootstrap.paaskafka-minipaas.svc:9092","topicName":"topic-bignum","consumerGroupId":"consumer-bignum"},"producerConfig":{"brokerUrl":"kf3efb60-bd08-4543-8cd8-b79fa17aadea-kafka-bootstrap.paaskafka-minipaas.svc:9092","topicName":"topic-bignum","msgLength":8,"msgCount":0, "sendTimeIntervalMs": 0}}' | json_pp

查询kafka任务列表
curl http://127.0.0.1:29080/kafkatesttool/kafka/task/list -X GET -H "Content-Type:application/json" | json_pp

删除kafka任务
curl http://127.0.0.1:29080/kafkatesttool/kafka/task/delete?taskName=task-monitor -X DELETE -H "Content-Type:application/json" | json_pp
```
