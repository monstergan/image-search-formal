package com.example.imagesearchformal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("img_search_rebuild_job")
public class ImgSearchRebuildJob {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String jobNo;
    private Long instanceId;
    private Integer jobType;
    private String scopeDesc;
    private Long snapshotMaxId;
    private Long totalCount;
    private Long successCount;
    private Long failCount;
    private Integer status;
    private String remark;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
