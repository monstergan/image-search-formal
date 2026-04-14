package com.example.imagesearchformal.client;

import com.example.imagesearchformal.domain.dto.ImageSearchDeleteRequest;
import com.example.imagesearchformal.domain.dto.ImageSearchQueryRequest;
import com.example.imagesearchformal.domain.dto.ImageSearchQueryResponse;
import com.example.imagesearchformal.domain.dto.ImageSearchUpsertRequest;

public interface ImageSearchClient {

    String upsert(ImageSearchUpsertRequest request);

    String delete(ImageSearchDeleteRequest request);

    ImageSearchQueryResponse searchByPic(ImageSearchQueryRequest request);

    boolean checkExists(String instanceName, String productId, String picName);
}
