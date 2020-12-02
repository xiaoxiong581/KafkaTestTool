package com.yzx.xiaoxiong581.kafkatesttool.microservice.domain.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yangzhixiong
 */
@Data
@NoArgsConstructor
public class ListTaskResponse {
    private int totalCount;

    private List<KafkaTaskDetail> datas;

    public ListTaskResponse(int totalCount, List<KafkaTaskDetail> datas) {
        this.totalCount = totalCount;
        this.datas = datas;
    }
}
