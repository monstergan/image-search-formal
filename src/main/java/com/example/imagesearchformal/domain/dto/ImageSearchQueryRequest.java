package com.example.imagesearchformal.domain.dto;

import lombok.Data;

@Data
public class ImageSearchQueryRequest {
    private String instanceName;
    private String imageUrl;
    private Integer categoryId;
    private Integer num;
    private Integer start;
    private String filter;
    private String scoreThreshold;
    private Boolean distinctProductId;
    private Boolean crop;
}
