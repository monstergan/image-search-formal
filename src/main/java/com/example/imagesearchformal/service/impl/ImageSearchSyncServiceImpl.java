package com.example.imagesearchformal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.imagesearchformal.config.ImageSearchProperties;
import com.example.imagesearchformal.domain.dto.ProductImageSyncDTO;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.entity.ImgSearchInstance;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;
import com.example.imagesearchformal.mapper.ImgSearchImageMapper;
import com.example.imagesearchformal.mapper.ImgSearchInstanceMapper;
import com.example.imagesearchformal.service.ImageSearchImageService;
import com.example.imagesearchformal.service.ImageSearchRebuildService;
import com.example.imagesearchformal.service.ImageSearchSyncService;
import com.example.imagesearchformal.service.ImageSearchTaskService;
import com.example.imagesearchformal.support.ImageFileFilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageSearchSyncServiceImpl implements ImageSearchSyncService {

    private final ImgSearchInstanceMapper instanceMapper;
    private final ImgSearchImageMapper imageMapper;
    private final ImageSearchImageService imageService;
    private final ImageSearchTaskService taskService;
    private final ImageSearchRebuildService rebuildService;
    private final RabbitTemplate rabbitTemplate;
    private final ImageSearchProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncImageOnChanged(ProductImageSyncDTO dto) {
        if (!ImageFileFilterUtil.isImageFile(dto.getImageUrl(), dto.getMimeType())) {
            log.info("非图片文件，跳过图搜同步，imageId={}, imageUrl={}", dto.getImageId(), dto.getImageUrl());
            return;
        }
        if (!ImageFileFilterUtil.isValidSize(dto.getWidth(), dto.getHeight())) {
            log.warn("图片尺寸不符合图搜要求，跳过图搜同步，imageId={}, width={}, height={}", dto.getImageId(), dto.getWidth(), dto.getHeight());
            return;
        }
        ImgSearchInstance instance = getEnabledInstance();
        ImgSearchImage image = imageService.saveOrUpdateImage(instance.getId(), dto);
        Long taskId = taskService.createTask(image, ImageTaskTypeEnum.UPSERT);
        rabbitTemplate.convertAndSend(properties.getMq().getExchange(), properties.getMq().getRoutingKey(), taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteImageOnRemoved(ProductImageSyncDTO dto) {
        ImgSearchInstance instance = getEnabledInstance();
        ImgSearchImage image = imageService.markDeleted(instance.getId(), dto.getImageId());
        if (image == null) {
            return;
        }
        Long taskId = taskService.createTask(image, ImageTaskTypeEnum.DELETE);
        rabbitTemplate.convertAndSend(properties.getMq().getExchange(), properties.getMq().getRoutingKey(), taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildSku(String sku) {
        ImgSearchInstance instance = getEnabledInstance();
        List<ImgSearchImage> imageList = imageMapper.selectList(
                new LambdaQueryWrapper<ImgSearchImage>()
                        .eq(ImgSearchImage::getInstanceId, instance.getId())
                        .eq(ImgSearchImage::getSku, sku)
                        .eq(ImgSearchImage::getDeleted, 0)
                        .eq(ImgSearchImage::getIndexableFlag, 1)
        );
        for (ImgSearchImage image : imageList) {
            image.setSyncStatus(0);
            image.setLastTaskType(ImageTaskTypeEnum.REBUILD.getCode());
            image.setVersion(image.getVersion() + 1);
            imageMapper.updateById(image);
            Long taskId = taskService.createTask(image, ImageTaskTypeEnum.REBUILD);
            rabbitTemplate.convertAndSend(properties.getMq().getExchange(), properties.getMq().getRoutingKey(), taskId);
        }
    }

    @Override
    public Long createFullRebuildJob() {
        return rebuildService.createFullRebuildJob(getEnabledInstance().getId());
    }

    private ImgSearchInstance getEnabledInstance() {
        ImgSearchInstance instance = instanceMapper.selectEnabledInstance(properties.getProvider());
        if (instance == null) {
            throw new RuntimeException("未查询到启用中的图搜实例");
        }
        return instance;
    }
}
