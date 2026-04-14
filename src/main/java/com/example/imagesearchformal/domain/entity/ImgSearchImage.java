package com.example.imagesearchformal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("img_search_image")
public class ImgSearchImage {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long instanceId;
    private String bizType;
    private String sku;
    private String spu;
    private Long imageId;
    private String productId;
    private String picName;
    private String imageUrl;
    private String ossBucket;
    private String ossObjectKey;
    private String imageMd5;
    private String mimeType;
    private String fileExt;
    private Long fileSize;
    private Integer width;
    private Integer height;
    private Integer mainFlag;
    private Long platformId;
    private Long siteId;
    private Long categoryId;
    private String categoryPath;
    private Integer indexableFlag;
    private Integer deleted;
    private Integer syncStatus;
    private Integer lastTaskType;
    private Integer version;
    private Integer bucketNo;
    private LocalDateTime lastSyncTime;
    private String lastRequestId;
    private String lastErrorCode;
    private String lastErrorMsg;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
