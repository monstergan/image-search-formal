package com.example.imagesearchformal.controller;

import com.example.imagesearchformal.common.Result;
import com.example.imagesearchformal.domain.dto.ProductImageSyncDTO;
import com.example.imagesearchformal.service.ImageSearchRebuildService;
import com.example.imagesearchformal.service.ImageSearchSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image-search/admin")
@RequiredArgsConstructor
public class ImageSearchAdminController {

    private final ImageSearchSyncService imageSearchSyncService;
    private final ImageSearchRebuildService imageSearchRebuildService;

    @PostMapping("/sync")
    public Result<String> sync(@Validated @RequestBody ProductImageSyncDTO dto) {
        imageSearchSyncService.syncImageOnChanged(dto);
        return Result.ok("同步任务已创建");
    }

    @PostMapping("/delete")
    public Result<String> delete(@Validated @RequestBody ProductImageSyncDTO dto) {
        imageSearchSyncService.deleteImageOnRemoved(dto);
        return Result.ok("删除任务已创建");
    }

    @PostMapping("/rebuild/sku/{sku}")
    public Result<String> rebuildSku(@PathVariable String sku) {
        imageSearchSyncService.rebuildSku(sku);
        return Result.ok("SKU重建任务已创建");
    }

    @PostMapping("/rebuild/full")
    public Result<Long> createFullRebuildJob() {
        return Result.ok(imageSearchSyncService.createFullRebuildJob());
    }

    @PostMapping("/rebuild/full/{jobId}/execute")
    public Result<String> executeFullRebuildJob(@PathVariable Long jobId) {
        imageSearchRebuildService.executeFullRebuildJob(jobId);
        return Result.ok("全量重建执行完成");
    }
}
