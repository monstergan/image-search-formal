package com.example.imagesearchformal.scheduler;

import com.example.imagesearchformal.config.ImageSearchProperties;
import com.example.imagesearchformal.domain.entity.ImgSearchSyncTask;
import com.example.imagesearchformal.mapper.ImgSearchSyncTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageSearchRetryScheduler {

    private final ImgSearchSyncTaskMapper taskMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ImageSearchProperties properties;

    @Scheduled(fixedDelay = 60000)
    public void retry() {
        List<ImgSearchSyncTask> taskList = taskMapper.selectRetryableTaskList(LocalDateTime.now(), 200);
        if (taskList == null || taskList.isEmpty()) {
            return;
        }
        for (ImgSearchSyncTask task : taskList) {
            rabbitTemplate.convertAndSend(properties.getMq().getExchange(), properties.getMq().getRoutingKey(), task.getId());
        }
    }
}
