package com.example.imagesearchformal.service.impl;

import com.example.imagesearchformal.domain.dto.ProductImageSyncDTO;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.enums.ImageSyncStatusEnum;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;
import com.example.imagesearchformal.mapper.ImgSearchImageMapper;
import com.example.imagesearchformal.service.ImageSearchImageService;
import com.example.imagesearchformal.support.ImageSearchIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageSearchImageServiceImpl implements ImageSearchImageService {

    private final ImgSearchImageMapper imageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImgSearchImage saveOrUpdateImage(Long instanceId, ProductImageSyncDTO dto) {
        ImgSearchImage image = imageMapper.selectByInstanceIdAndImageId(instanceId, dto.getImageId());
        if (image == null) {
            image = new ImgSearchImage();
            image.setInstanceId(instanceId);
            image.setBizType("SKU_IMAGE");
            image.setVersion(1);
            image.setBucketNo(ImageSearchIdUtil.bucketNo(dto.getImageId()));
            image.setProductId(ImageSearchIdUtil.buildProductId(dto.getSku()));
            image.setPicName(ImageSearchIdUtil.buildPicName(dto.getImageId()));
            fillCommon(image, dto);
            imageMapper.insert(image);
            return image;
        }
        fillCommon(image, dto);
        image.setProductId(ImageSearchIdUtil.buildProductId(dto.getSku()));
        image.setPicName(ImageSearchIdUtil.buildPicName(dto.getImageId()));
        image.setVersion(image.getVersion() + 1);
        imageMapper.updateById(image);
        return image;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImgSearchImage markDeleted(Long instanceId, Long imageId) {
        ImgSearchImage image = imageMapper.selectByInstanceIdAndImageId(instanceId, imageId);
        if (image == null) {
            return null;
        }
        image.setDeleted(1);
        image.setSyncStatus(ImageSyncStatusEnum.PENDING.getCode());
        image.setLastTaskType(ImageTaskTypeEnum.DELETE.getCode());
        image.setVersion(image.getVersion() + 1);
        imageMapper.updateById(image);
        return image;
    }

    @Override
    public ImgSearchImage getById(Long id) {
        return imageMapper.selectById(id);
    }

    @Override
    public ImgSearchImage getByInstanceIdAndImageId(Long instanceId, Long imageId) {
        return imageMapper.selectByInstanceIdAndImageId(instanceId, imageId);
    }

    @Override
    public ImgSearchImage getByProductIdAndPicName(Long instanceId, String productId, String picName) {
        return imageMapper.selectByProductIdAndPicName(instanceId, productId, picName);
    }

    @Override
    public void markSyncing(Long imagePkId) {
        imageMapper.updateSyncing(imagePkId);
    }

    @Override
    public void markSyncSuccess(Long imagePkId, String requestId) {
        imageMapper.updateSyncSuccess(imagePkId, requestId);
    }

    @Override
    public void markSyncDeleted(Long imagePkId, String requestId) {
        imageMapper.updateSyncDeleted(imagePkId, requestId);
    }

    @Override
    public void markSyncFail(Long imagePkId, String errorCode, String errorMsg) {
        imageMapper.updateSyncFail(imagePkId, errorCode, errorMsg);
    }

    private void fillCommon(ImgSearchImage image, ProductImageSyncDTO dto) {
        image.setSku(dto.getSku());
        image.setSpu(dto.getSpu());
        image.setImageId(dto.getImageId());
        image.setImageUrl(dto.getImageUrl());
        image.setOssBucket(dto.getOssBucket());
        image.setOssObjectKey(dto.getOssObjectKey());
        image.setImageMd5(dto.getImageMd5());
        image.setMimeType(dto.getMimeType());
        image.setFileExt(dto.getFileExt());
        image.setFileSize(dto.getFileSize());
        image.setWidth(dto.getWidth());
        image.setHeight(dto.getHeight());
        image.setMainFlag(Boolean.TRUE.equals(dto.getMainFlag()) ? 1 : 0);
        image.setPlatformId(dto.getPlatformId());
        image.setSiteId(dto.getSiteId());
        image.setCategoryId(dto.getCategoryId());
        image.setCategoryPath(dto.getCategoryPath());
        image.setIndexableFlag(1);
        image.setDeleted(0);
        image.setSyncStatus(ImageSyncStatusEnum.PENDING.getCode());
        image.setLastTaskType(ImageTaskTypeEnum.UPSERT.getCode());
    }
}
