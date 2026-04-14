package com.example.imagesearchformal.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImageSearchQueryResponse {
    private String requestId;
    private Integer docsFound;
    private Integer docsReturn;
    private Integer searchTime;
    private List<ImageSearchHitDTO> hits;
}
