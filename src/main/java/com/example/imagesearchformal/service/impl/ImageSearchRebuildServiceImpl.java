package com.example.imagesearchformal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.imagesearchformal.config.ImageSearchProperties;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import com.example.imagesearchformal.domain.entity.ImgSearchRebuildJob;
import com.example.imagesearchformal.domain.enums.ImageTaskTypeEnum;
import com.example.imagesearchformal.mapper.ImgSearchImageMapper;
import com.example.imagesearchformal.mapper.ImgSearchRebuildJobMapper;
import com.example.imagesearchformal.service.ImageSearchRebuildService;
import com.example.imagesearchformal.service.ImageSearchTaskService;
import com.example.imagesearchformal.support.ImageSearchIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSearchRebuildServiceImpl implements ImageSearchRebuildService {

    private final ImgSearchImageMapper imageMapper;
    private final ImgSearchRebuildJobMapper rebuildJobMapper;
    private final ImageSearchTaskService taskService;
    private final RabbitTemplate rabbitTemplate;
    private final ImageSearchProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFullRebuildJob(Long instanceId) {
        ImgSearchImage lastOne = imageMapper.selectOne(
                new LambdaQueryWrapper<ImgSearchImage>()
                        .eq(ImgSearchImage::getInstanceId, instanceId)
                        .orderByDesc(ImgSearchImage::getId)
                        .last("limit 1")
        );
        Long snapshotMaxId = lastOne == null ? 0L : lastOne.getId();
        Long total = Long.valueOf(imageMapper.selectCount(
                new LambdaQueryWrapper<ImgSearchImage>()
                        .eq(ImgSearchImage::getInstanceId, instanceId)
                        .le(ImgSearchImage::getId, snapshotMaxId)
                        .eq(ImgSearchImage::getDeleted, 0)
                        .eq(ImgSearchImage::getIndexableFlag, 1)
        ));
        ImgSearchRebuildJob job = new ImgSearchRebuildJob();
        job.setJobNo("TMP");
        job.setInstanceId(instanceId);
        job.setJobType(1);
        job.setScopeDesc("全量重建");
        job.setSnapshotMaxId(snapshotMaxId);
        job.setTotalCount(total);
        job.setSuccessCount(0L);
        job.setFailCount(0L);
        job.setStatus(0);
        rebuildJobMapper.insert(job);
        job.setJobNo(ImageSearchIdUtil.buildRebuildJobNo(job.getId()));
        rebuildJobMapper.updateById(job);
        return job.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeFullRebuildJob(Long jobId) {
        ImgSearchRebuildJob job = rebuildJobMapper.selectById(jobId);
        if (job == null) {
            return;
        }
        if (rebuildJobMapper.markRunning(jobId) <= 0) {
            return;
        }
        long lastId = 0L;
        int pageSize = 1000;
        while (true) {
            List<ImgSearchImage> list = imageMapper.selectPageForRebuild(job.getInstanceId(), lastId, job.getSnapshotMaxId(), pageSize);
            if (list == null || list.isEmpty()) {
                break;
            }
            int success = 0;
            int fail = 0;
            for (ImgSearchImage image : list) {
                try {
                    image.setSyncStatus(0);
                    image.setLastTaskType(ImageTaskTypeEnum.REBUILD.getCode());
                    image.setVersion(image.getVersion() + 1);
                    imageMapper.updateById(image);
                    Long taskId = taskService.createTask(image, ImageTaskTypeEnum.REBUILD);
                    rabbitTemplate.convertAndSend(properties.getMq().getExchange(), properties.getMq().getRoutingKey(), taskId);
                    success++;
                } catch (Exception e) {
                    fail++;
                }
                lastId = image.getId();
            }
            if (success > 0) {
                rebuildJobMapper.addSuccessCount(jobId, success);
            }
            if (fail > 0) {
                rebuildJobMapper.addFailCount(jobId, fail);
            }
        }
        rebuildJobMapper.markFinished(jobId);
    }
}
