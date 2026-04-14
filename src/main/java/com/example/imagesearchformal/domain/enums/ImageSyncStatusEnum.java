package com.example.imagesearchformal.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSyncStatusEnum {
    PENDING(0, "待同步"),
    SYNCING(1, "同步中"),
    SUCCESS(2, "已同步"),
    FAIL(3, "失败"),
    DELETED(4, "已删除");

    private final Integer code;
    private final String desc;
}
