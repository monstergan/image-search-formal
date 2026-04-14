package com.example.imagesearchformal.config;

import com.aliyun.imagesearch20201214.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunImageSearchConfig {

    @Bean
    public Client aliyunImageSearchSdkClient(ImageSearchProperties properties) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(properties.getAccessKeyId());
        config.setAccessKeySecret(properties.getAccessKeySecret());
        config.setRegionId(properties.getRegionId());
        config.setEndpoint(properties.getEndpoint());
        config.setType("access_key");
        return new Client(config);
    }
}
