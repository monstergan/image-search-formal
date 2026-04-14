package com.example.imagesearchformal.consumer;

import com.example.imagesearchformal.client.ImageSearchClient;
import com.example.imagesearchformal.config.ImageSearchProperties;
import com.example.imagesearchformal.domain.dto.ImageSearchDeleteRequest;
import com.example.imagesearchformal.domain.dto.ImageSearchUpsertRequest;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.entity.ImgSearchInstance;
import com.example.imagesearchformal.domain.entity.ImgSearchSyncTask;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;
import com.example.imagesearchformal.domain.enums.TaskStatusEnum;
import com.example.imagesearchformal.mapper.ImgSearchInstanceMapper;
import com.example.imagesearchformal.service.ImageSearchImageService;
import com.example.imagesearchformal.service.ImageSearchTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageSearchTaskConsumer {

    private final ImgSearchInstanceMapper instanceMapper;
    private final ImageSearchTaskService taskService;
    private final ImageSearchImageService imageService;
    private final ImageSearchClient imageSearchClient;
    private final ImageSearchProperties properties;

    @RabbitListener(queues = "${image-search.mq.queue}")
    public void consume(Long taskId) {
        ImgSearchSyncTask task = taskService.getById(taskId);
        if (task == null) {
            return;
        }
        if (!TaskStatusEnum.PENDING.getCode().equals(task.getStatus())
                && !TaskStatusEnum.RETRY.getCode().equals(task.getStatus())) {
            return;
        }

        ImgSearchImage image = imageService.getById(task.getImagePkId());
        if (image == null) {
            taskService.markCanceled(taskId, "图片主记录不存在");
            return;
        }
        if (!image.getVersion().equals(task.getTargetVersion())) {
            taskService.markCanceled(taskId, "任务版本过期，当前version=" + image.getVersion());
            return;
        }
        if (!taskService.markRunning(taskId)) {
            return;
        }
        imageService.markSyncing(image.getId());

        ImgSearchInstance instance = instanceMapper.selectById(task.getInstanceId());
        if (instance == null) {
            taskService.markDead(taskId, "INSTANCE_NOT_FOUND", "实例不存在");
            imageService.markSyncFail(image.getId(), "INSTANCE_NOT_FOUND", "实例不存在");
            return;
        }

        try {
            if (ImageTaskTypeEnum.UPSERT.getCode().equals(task.getTaskType())
                    || ImageTaskTypeEnum.REBUILD.getCode().equals(task.getTaskType())) {
                ImageSearchUpsertRequest req = new ImageSearchUpsertRequest();
                req.setInstanceName(instance.getInstanceName());
                req.setProductId(image.getProductId());
                req.setPicName(image.getPicName());
                req.setImageUrl(image.getImageUrl());
                req.setCategoryId(image.getCategoryId() == null ? null : image.getCategoryId().intValue());
                req.setCustomContent(image.getSku());
                req.setIntAttr(image.getMainFlag());
                req.setIntAttr2(image.getPlatformId() == null ? null : image.getPlatformId().intValue());
                req.setIntAttr3(image.getSiteId() == null ? null : image.getSiteId().intValue());
                req.setIntAttr4(image.getCategoryId() == null ? null : image.getCategoryId().intValue());
                req.setStrAttr(image.getBizType());
                req.setCrop(properties.getCrop());
                String requestId = imageSearchClient.upsert(req);
                imageService.markSyncSuccess(image.getId(), requestId);
                taskService.markSuccess(taskId, requestId);
                return;
            }
            if (ImageTaskTypeEnum.DELETE.getCode().equals(task.getTaskType())) {
                ImageSearchDeleteRequest req = new ImageSearchDeleteRequest();
                req.setInstanceName(instance.getInstanceName());
                req.setProductId(image.getProductId());
                req.setPicName(image.getPicName());
                String requestId = imageSearchClient.delete(req);
                imageService.markSyncDeleted(image.getId(), requestId);
                taskService.markSuccess(taskId, requestId);
            }
        } catch (Exception e) {
            log.error("图搜任务执行失败，taskId={}", taskId, e);
            int nextRetry = task.getRetryCount() + 1;
            if (nextRetry >= task.getMaxRetryCount()) {
                taskService.markDead(taskId, "IMG_SEARCH_ERROR", e.getMessage());
                imageService.markSyncFail(image.getId(), "IMG_SEARCH_ERROR", e.getMessage());
            } else {
                taskService.markRetry(taskId, "IMG_SEARCH_ERROR", e.getMessage());
                imageService.markSyncFail(image.getId(), "IMG_SEARCH_ERROR", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
