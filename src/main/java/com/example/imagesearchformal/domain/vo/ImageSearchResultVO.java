package com.example.imagesearchformal.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ImageSearchResultVO {
    private String sku;
    private Long imageId;
    private String imageUrl;
    private BigDecimal score;
    private Long categoryId;
    private Long platformId;
    private Long siteId;
    private Integer mainFlag;
}
