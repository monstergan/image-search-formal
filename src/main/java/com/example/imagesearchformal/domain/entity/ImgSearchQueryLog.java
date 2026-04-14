package com.example.imagesearchformal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("img_search_query_log")
public class ImgSearchQueryLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String queryNo;
    private Long instanceId;
    private String bizScene;
    private Integer querySource;
    private String queryImageMd5;
    private String queryImageUrl;
    private BigDecimal thresholdScore;
    private Integer topK;
    private String filterExpr;
    private Integer docsFound;
    private Integer docsReturn;
    private Integer searchTimeMs;
    private String top1ProductId;
    private String top1PicName;
    private BigDecimal top1Score;
    private String requestId;
    private Long operatorId;
    private String operatorName;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
