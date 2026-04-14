package com.example.imagesearchformal.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class ImageSearchQueryCmd {
    @NotBlank(message = "imageUrl不能为空")
    private String imageUrl;
    private BigDecimal scoreThreshold;
    private Integer topK;
    private Long platformId;
    private Long siteId;
    private Long categoryId;
    private Boolean mainFlag;
    private Long operatorId;
    private String operatorName;
}
