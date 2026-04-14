package com.example.imagesearchformal.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RebuildJobStatusEnum {
    PENDING(0, "待执行"),
    RUNNING(1, "执行中"),
    FINISHED(2, "已完成"),
    STOPPED(3, "已终止");

    private final Integer code;
    private final String desc;
}
