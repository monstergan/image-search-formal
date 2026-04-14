package com.example.imagesearchformal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "image-search")
public class ImageSearchProperties {

    private Boolean enabled = Boolean.TRUE;
    private String provider = "ALIYUN";
    private String regionId;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String instanceName;
    private String bizType = "SKU_IMAGE";
    private Double defaultScoreThreshold = 0.85D;
    private Integer defaultTopK = 20;
    private Boolean searchDistinctProduct = Boolean.TRUE;
    private Boolean crop = Boolean.TRUE;
    private Integer maxRetryCount = 5;

    private Mq mq = new Mq();

    @Data
    public static class Mq {
        private String exchange = "img.search.exchange";
        private String routingKey = "img.search.sync";
        private String queue = "img.search.sync.queue";
    }
}
