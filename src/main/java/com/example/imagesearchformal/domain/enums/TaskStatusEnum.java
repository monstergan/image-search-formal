package com.example.imagesearchformal.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    PENDING(0, "待执行"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "成功"),
    RETRY(3, "失败待重试"),
    DEAD(4, "终态失败"),
    CANCELED(5, "取消");

    private final Integer code;
    private final String desc;
}
