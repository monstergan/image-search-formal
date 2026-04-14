package com.example.imagesearchformal.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageTaskTypeEnum {
    UPSERT(1, "新增/更新"),
    DELETE(2, "删除"),
    REBUILD(3, "重建"),
    CHECK(4, "校验");

    private final Integer code;
    private final String desc;
}
