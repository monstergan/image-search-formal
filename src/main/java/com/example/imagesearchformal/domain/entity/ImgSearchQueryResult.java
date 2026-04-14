package com.example.imagesearchformal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("img_search_query_result")
public class ImgSearchQueryResult {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long queryLogId;
    private Integer rankNo;
    private String productId;
    private String picName;
    private String sku;
    private Long imageId;
    private BigDecimal score;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
