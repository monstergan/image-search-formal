package com.example.imagesearchformal.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.imagesearchformal.client.ImageSearchClient;
import com.example.imagesearchformal.config.ImageSearchProperties;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.entity.ImgSearchInstance;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;
import com.example.imagesearchformal.mapper.ImgSearchImageMapper;
import com.example.imagesearchformal.mapper.ImgSearchInstanceMapper;
import com.example.imagesearchformal.service.ImageSearchTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageSearchCheckScheduler {

    private final ImgSearchImageMapper imageMapper;
    private final ImgSearchInstanceMapper instanceMapper;
    private final ImageSearchClient imageSearchClient;
    private final ImageSearchTaskService taskService;
    private final RabbitTemplate rabbitTemplate;
    private final ImageSearchProperties properties;

    @Scheduled(cron = "0 30 2 * * ?")
    public void checkRandomSample() {
        ImgSearchInstance instance = instanceMapper.selectEnabledInstance(properties.getProvider());
        if (instance == null) {
            return;
        }
        List<ImgSearchImage> sampleList = imageMapper.selectList(
                new LambdaQueryWrapper<ImgSearchImage>()
                        .eq(ImgSearchImage::getInstanceId, instance.getId())
                        .eq(ImgSearchImage::getSyncStatus, 2)
                        .last("limit 100")
        );
        for (ImgSearchImage image : sampleList) {
            boolean exists = imageSearchClient.checkExists(instance.getInstanceName(), image.getProductId(), image.getPicName());
            if (!exists) {
                image.setSyncStatus(0);
                image.setLastTaskType(ImageTaskTypeEnum.CHECK.getCode());
                image.setVersion(image.getVersion() + 1);
                imageMapper.updateById(image);
                Long taskId = taskService.createTask(image, ImageTaskTypeEnum.REBUILD);
                rabbitTemplate.convertAndSend(properties.getMq().getExchange(), properties.getMq().getRoutingKey(), taskId);
            }
        }
    }
}
