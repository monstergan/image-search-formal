package com.example.imagesearchformal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("img_search_instance")
public class ImgSearchInstance {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String provider;
    private String regionId;
    private String endpoint;
    private String instanceName;
    private Integer enabled;
    private Integer addQps;
    private Integer searchQps;
    private Integer deleteQps;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
