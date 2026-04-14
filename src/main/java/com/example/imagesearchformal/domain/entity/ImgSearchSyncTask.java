package com.example.imagesearchformal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("img_search_sync_task")
public class ImgSearchSyncTask {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String taskNo;
    private Long instanceId;
    private Integer taskType;
    private String bizType;
    private Long imagePkId;
    private Long imageId;
    private String sku;
    private String productId;
    private String picName;
    private Integer targetVersion;
    private Integer status;
    private Integer retryCount;
    private Integer maxRetryCount;
    private LocalDateTime nextRetryTime;
    private String traceId;
    private String requestId;
    private String errorCode;
    private String errorMsg;
    private String payloadJson;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
