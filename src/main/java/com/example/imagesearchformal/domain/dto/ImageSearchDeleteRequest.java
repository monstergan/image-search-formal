package com.example.imagesearchformal.domain.dto;

import lombok.Data;

@Data
public class ImageSearchDeleteRequest {
    private String instanceName;
    private String productId;
    private String picName;
}
