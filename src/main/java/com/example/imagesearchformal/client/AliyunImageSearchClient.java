package com.example.imagesearchformal.client;

import com.aliyun.imagesearch20201214.Client;
import com.aliyun.imagesearch20201214.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.example.imagesearchformal.domain.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunImageSearchClient implements ImageSearchClient {

    private final Client client;

    @Override
    public String upsert(ImageSearchUpsertRequest request) {
        InputStream inputStream = null;
        try {
            inputStream = new URL(request.getImageUrl()).openStream();
            AddImageAdvanceRequest req = new AddImageAdvanceRequest();
            req.setInstanceName(request.getInstanceName());
            req.setProductId(request.getProductId());
            req.setPicName(request.getPicName());
            req.setPicContentObject(inputStream);
            req.setCategoryId(request.getCategoryId());
            req.setCustomContent(request.getCustomContent());
            req.setIntAttr(request.getIntAttr());
            req.setIntAttr2(request.getIntAttr2());
            req.setIntAttr3(request.getIntAttr3());
            req.setIntAttr4(request.getIntAttr4());
            req.setStrAttr(request.getStrAttr());
            req.setCrop(request.getCrop());
            AddImageResponse response = client.addImageAdvance(req, new RuntimeOptions());
            return response.getBody().getRequestId();
        } catch (Exception e) {
            throw new RuntimeException("阿里云图搜Add失败: " + e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public String delete(ImageSearchDeleteRequest request) {
        try {
            DeleteImageRequest req = new DeleteImageRequest();
            req.setInstanceName(request.getInstanceName());
            req.setProductId(request.getProductId());
            req.setPicName(request.getPicName());
            DeleteImageResponse response = client.deleteImage(req);
            return response.getBody().getRequestId();
        } catch (Exception e) {
            throw new RuntimeException("阿里云图搜Delete失败: " + e.getMessage(), e);
        }
    }

    @Override
    public ImageSearchQueryResponse searchByPic(ImageSearchQueryRequest request) {
        InputStream inputStream = null;
        try {
            inputStream = new URL(request.getImageUrl()).openStream();
            SearchImageByPicAdvanceRequest req = new SearchImageByPicAdvanceRequest();
            req.setInstanceName(request.getInstanceName());
            req.setPicContentObject(inputStream);
            req.setCategoryId(request.getCategoryId());
            req.setNum(request.getNum());
            req.setStart(request.getStart());
            req.setFilter(request.getFilter());
            req.setScoreThreshold(request.getScoreThreshold());
            req.setDistinctProductId(request.getDistinctProductId());
            req.setCrop(request.getCrop());

            SearchImageByPicResponse resp = client.searchImageByPicAdvance(req, new RuntimeOptions());
            ImageSearchQueryResponse response = new ImageSearchQueryResponse();
            response.setRequestId(resp.getBody().getRequestId());
            if (resp.getBody().getHead() != null) {
                response.setDocsFound(resp.getBody().getHead().getDocsFound());
                response.setDocsReturn(resp.getBody().getHead().getDocsReturn());
                response.setSearchTime(resp.getBody().getHead().getSearchTime());
            }
            List<ImageSearchHitDTO> hits = new ArrayList<ImageSearchHitDTO>();
            if (resp.getBody().getAuctions() != null) {
                for (SearchImageByPicResponseBody.SearchImageByPicResponseBodyAuctions item : resp.getBody().getAuctions()) {
                    ImageSearchHitDTO dto = new ImageSearchHitDTO();
                    dto.setProductId(item.getProductId());
                    dto.setPicName(item.getPicName());
                    dto.setScore(new BigDecimal(String.valueOf(item.getScore())));
                    dto.setCategoryId(item.getCategoryId());
                    dto.setCustomContent(item.getCustomContent());
                    dto.setIntAttr(item.getIntAttr());
                    dto.setIntAttr2(item.getIntAttr2());
                    dto.setIntAttr3(item.getIntAttr3());
                    dto.setIntAttr4(item.getIntAttr4());
                    dto.setStrAttr(item.getStrAttr());
                    hits.add(dto);
                }
            }
            response.setHits(hits);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("阿里云图搜SearchImageByPic失败: " + e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public boolean checkExists(String instanceName, String productId, String picName) {
        try {
            CheckImageExistsRequest req = new CheckImageExistsRequest();
            req.setInstanceName(instanceName);
            req.setProductId(productId);
            req.setPicName(picName);
            CheckImageExistsResponse response = client.checkImageExists(req);
            return Boolean.TRUE.equals(response.getBody().getExists());
        } catch (Exception e) {
            throw new RuntimeException("阿里云图搜CheckImageExists失败: " + e.getMessage(), e);
        }
    }
}
