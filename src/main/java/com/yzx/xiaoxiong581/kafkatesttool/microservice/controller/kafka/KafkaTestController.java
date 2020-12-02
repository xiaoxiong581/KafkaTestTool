package com.yzx.xiaoxiong581.kafkatesttool.microservice.controller.kafka;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.BaseResponse;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka.KafkaTask;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka.KafkaTaskDetail;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka.ListTaskResponse;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.exception.error.ResultErrorEnum;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.kafka.KafkaConsumerAdapter;
import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.kafka.KafkaProducerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author yangzhixiong
 */
@Slf4j
@RestController
@RequestMapping("/kafka/task")
public class KafkaTestController {
    private final int MAX_NUM_TASK = 10;

    @Autowired
    private BeanFactory beanFactory;

    private Map<String, KafkaTask> cacheTasks = new ConcurrentHashMap<>();

    private Map<String, List<Future>> cacheTaskFutures = new ConcurrentHashMap<>();

    private ThreadPoolExecutor taskPoolExecutor = new
            ThreadPoolExecutor(MAX_NUM_TASK * 2, MAX_NUM_TASK * 2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void cleanDonedTask() {
        Iterator ite = cacheTaskFutures.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, List<Future>> task = (Map.Entry<String, List<Future>>) ite.next();
            boolean isDone = true;
            for (Future future : task.getValue()) {
                if (!future.isDone()) {
                    isDone = false;
                    break;
                }
            }
            if (isDone) {
                log.info("task {} execute finish, remove", task.getKey());
                cacheTasks.remove(task.getKey());
                cacheTaskFutures.remove(task.getKey());
            }
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse createTask(@RequestBody KafkaTask kafkaTask) {
        log.info("create new task, info: {}", kafkaTask);
        if (StringUtils.isEmpty(kafkaTask.getTaskName())) {
            String error = "taskName is null";
            log.error(error);
            return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
        }
        if (cacheTasks.containsKey(kafkaTask.getTaskName())) {
            String error = "taskName is exist, please change name";
            log.error(error);
            return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
        }
        if (null == kafkaTask.getConsumerConfig() && null == kafkaTask.getProducerConfig()) {
            String error = "consumerConfig and producerConfig cann't all null";
            log.error(error);
            return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
        }
        if (null != kafkaTask.getConsumerConfig()) {
            if (StringUtils.isEmpty(kafkaTask.getConsumerConfig().getBrokerUrl()) || StringUtils.isEmpty(kafkaTask.getConsumerConfig().getTopicName()) || StringUtils.isEmpty(kafkaTask.getConsumerConfig().getConsumerGroupId())) {
                String error = "brokerUrl or topicName or consumerGroupId cann't be null in consumerConfig";
                log.error(error);
                return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
            }
        }
        if (null != kafkaTask.getProducerConfig()) {
            if (StringUtils.isEmpty(kafkaTask.getProducerConfig().getBrokerUrl()) || StringUtils.isEmpty(kafkaTask.getProducerConfig().getTopicName()) || kafkaTask.getProducerConfig().getMsgCount() < 0 || kafkaTask.getProducerConfig().getMsgLength() < 1) {
                String error = "brokerUrl or topicName cann't be null or msgCount less 0 or msgLength less 1 in consumerConfig";
                log.error(error);
                return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
            }
        }
        if (cacheTasks.size() >= MAX_NUM_TASK) {
            String error = "current tasks is more than 10, please delete task or wait task finish";
            log.error(error);
            return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
        }
        List<Future> futures = new ArrayList<>();
        if (null != kafkaTask.getConsumerConfig()) {
            Future taskFuture = taskPoolExecutor.submit(new KafkaConsumerAdapter(kafkaTask.getTaskName(), kafkaTask.getConsumerConfig()));
            futures.add(taskFuture);
        }
        if (null != kafkaTask.getProducerConfig()) {
            if (null != kafkaTask.getConsumerConfig()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
            }
            Future taskFuture = taskPoolExecutor.submit(new KafkaProducerAdapter(kafkaTask.getTaskName(), kafkaTask.getProducerConfig()));
            futures.add(taskFuture);
        }
        cacheTasks.put(kafkaTask.getTaskName(), kafkaTask);
        cacheTaskFutures.put(kafkaTask.getTaskName(), futures);
        return new BaseResponse();

    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ListTaskResponse listTask() {
        List<KafkaTask> tasks = new ArrayList<>(cacheTasks.values());
        List<KafkaTaskDetail> results = new ArrayList<>();
        for (KafkaTask task : tasks) {
            KafkaTaskDetail taskDetail = new KafkaTaskDetail();
            BeanUtils.copyProperties(task, taskDetail);
            if (cacheTaskFutures.containsKey(task.getTaskName())) {
                boolean isDone = true;
                for (Future future : cacheTaskFutures.get(task.getTaskName())) {
                    if (!future.isDone()) {
                        isDone = false;
                        break;
                    }
                }

                if (!isDone) {
                    taskDetail.setStatus("executing");
                }
            }
            results.add(taskDetail);
        }
        return new ListTaskResponse(results.size(), results);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTask(@RequestParam("taskName") String taskName) {
        log.info("delete task, taskName: {}", taskName);
        if (!cacheTasks.containsKey(taskName)) {
            String error = "taskName is not exist";
            log.error(error);
            return new BaseResponse(ResultErrorEnum.PARAM_VALID_ERROR.getCode(), error);
        }

        cacheTasks.remove(taskName);
        if (cacheTaskFutures.containsKey(taskName)) {
            for (Future future : cacheTaskFutures.get(taskName)) {
                future.cancel(true);
            }
            cacheTaskFutures.remove(taskName);
        }
        return new BaseResponse();
    }
}
