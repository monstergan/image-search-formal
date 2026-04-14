package com.example.imagesearchformal.service;

import com.example.imagesearchformal.domain.dto.ProductImageSyncDTO;

public interface ImageSearchSyncService {
    void syncImageOnChanged(ProductImageSyncDTO dto);
    void deleteImageOnRemoved(ProductImageSyncDTO dto);
    void rebuildSku(String sku);
    Long createFullRebuildJob();
}
