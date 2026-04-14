package com.example.imagesearchformal.service;

import com.example.imagesearchformal.domain.dto.ProductImageSyncDTO;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;

public interface ImageSearchImageService {
    ImgSearchImage saveOrUpdateImage(Long instanceId, ProductImageSyncDTO dto);
    ImgSearchImage markDeleted(Long instanceId, Long imageId);
    ImgSearchImage getById(Long id);
    ImgSearchImage getByInstanceIdAndImageId(Long instanceId, Long imageId);
    ImgSearchImage getByProductIdAndPicName(Long instanceId, String productId, String picName);
    void markSyncing(Long imagePkId);
    void markSyncSuccess(Long imagePkId, String requestId);
    void markSyncDeleted(Long imagePkId, String requestId);
    void markSyncFail(Long imagePkId, String errorCode, String errorMsg);
}
