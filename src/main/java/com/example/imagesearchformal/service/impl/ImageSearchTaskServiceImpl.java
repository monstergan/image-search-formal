package com.example.imagesearchformal.service.impl;

import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.entity.ImgSearchSyncTask;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;
import com.example.imagesearchformal.mapper.ImgSearchSyncTaskMapper;
import com.example.imagesearchformal.service.ImageSearchTaskService;
import com.example.imagesearchformal.support.ImageSearchIdUtil;
import com.example.imagesearchformal.support.RetryBackoffUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageSearchTaskServiceImpl implements ImageSearchTaskService {

    private final ImgSearchSyncTaskMapper taskMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Long createTask(ImgSearchImage image, ImageTaskTypeEnum taskType) {
        try {
            ImgSearchSyncTask task = new ImgSearchSyncTask();
            task.setTaskNo("TMP");
            task.setInstanceId(image.getInstanceId());
            task.setTaskType(taskType.getCode());
            task.setBizType(image.getBizType());
            task.setImagePkId(image.getId());
            task.setImageId(image.getImageId());
            task.setSku(image.getSku());
            task.setProductId(image.getProductId());
            task.setPicName(image.getPicName());
            task.setTargetVersion(image.getVersion());
            task.setStatus(0);
            task.setRetryCount(0);
            task.setMaxRetryCount(5);
            task.setPayloadJson(objectMapper.writeValueAsString(image));
            taskMapper.insert(task);
            task.setTaskNo(ImageSearchIdUtil.buildTaskNo(task.getId()));
            taskMapper.updateById(task);
            return task.getId();
        } catch (Exception e) {
            throw new RuntimeException("创建图搜任务失败: " + e.getMessage(), e);
        }
    }

    @Override
    public ImgSearchSyncTask getById(Long taskId) {
        return taskMapper.selectById(taskId);
    }

    @Override
    public boolean markRunning(Long taskId) {
        return taskMapper.markRunning(taskId) > 0;
    }

    @Override
    public void markSuccess(Long taskId, String requestId) {
        taskMapper.markSuccess(taskId, requestId);
    }

    @Override
    public void markRetry(Long taskId, String errorCode, String errorMsg) {
        ImgSearchSyncTask task = taskMapper.selectById(taskId);
        taskMapper.markRetry(taskId, errorCode, errorMsg, RetryBackoffUtil.nextRetryTime(task.getRetryCount()));
    }

    @Override
    public void markDead(Long taskId, String errorCode, String errorMsg) {
        taskMapper.markDead(taskId, errorCode, errorMsg);
    }

    @Override
    public void markCanceled(Long taskId, String reason) {
        taskMapper.markCanceled(taskId, reason);
    }
}
