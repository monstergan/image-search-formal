package com.example.imagesearchformal.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImageSearchHitDTO {
    private String productId;
    private String picName;
    private BigDecimal score;
    private Integer categoryId;
    private String customContent;
    private Integer intAttr;
    private Integer intAttr2;
    private Integer intAttr3;
    private Integer intAttr4;
    private String strAttr;
}
