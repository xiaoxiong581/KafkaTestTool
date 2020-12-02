package com.yzx.xiaoxiong581.kafkatesttool.microservice.controller.kafka;

import com.yzx.xiaoxiong581.kafkatesttool.microservice.adapter.kafka.KafkaConsumerAdapter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KafkaTestControllerTest {
    private ThreadPoolExecutor taskPoolExecutor = new
            ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    @Test
    public void testFutureStatus() {
       /*Future future =  taskPoolExecutor.submit(()->{
            System.out.printf("%s begin to execute task\n",
                    DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            try {
                while(true) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.printf("%s end to execute task\n",
                    DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        });*/
        Future future = taskPoolExecutor.submit(new KafkaConsumerAdapter("dddd", null));
       while (true) {
           System.out.printf("%s future status %s\n",
                   DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), future.isDone());
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }
}