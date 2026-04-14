package com.example.imagesearchformal.service;

import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.entity.ImgSearchSyncTask;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;

public interface ImageSearchTaskService {
    Long createTask(ImgSearchImage image, ImageTaskTypeEnum taskType);
    ImgSearchSyncTask getById(Long taskId);
    boolean markRunning(Long taskId);
    void markSuccess(Long taskId, String requestId);
    void markRetry(Long taskId, String errorCode, String errorMsg);
    void markDead(Long taskId, String errorCode, String errorMsg);
    void markCanceled(Long taskId, String reason);
}
