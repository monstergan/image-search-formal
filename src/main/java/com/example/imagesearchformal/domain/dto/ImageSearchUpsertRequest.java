package com.example.imagesearchformal.domain.dto;

import lombok.Data;

@Data
public class ImageSearchUpsertRequest {
    private String instanceName;
    private String productId;
    private String picName;
    private String imageUrl;
    private Integer categoryId;
    private String customContent;
    private Integer intAttr;
    private Integer intAttr2;
    private Integer intAttr3;
    private Integer intAttr4;
    private String strAttr;
    private Boolean crop;
}
