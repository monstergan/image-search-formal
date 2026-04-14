package com.example.imagesearchformal.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductImageSyncDTO {

    @NotBlank(message = "sku不能为空")
    private String sku;
    private String spu;
    @NotNull(message = "imageId不能为空")
    private Long imageId;
    @NotBlank(message = "imageUrl不能为空")
    private String imageUrl;
    private String ossBucket;
    private String ossObjectKey;
    private String imageMd5;
    private String mimeType;
    private String fileExt;
    private Long fileSize;
    private Integer width;
    private Integer height;
    private Boolean mainFlag;
    private Long platformId;
    private Long siteId;
    private Long categoryId;
    private String categoryPath;
    private Boolean deleted;
}
