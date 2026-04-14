package com.example.imagesearchformal.service.impl;

import com.example.imagesearchformal.client.ImageSearchClient;
import com.example.imagesearchformal.config.ImageSearchProperties;
import com.example.imagesearchformal.domain.dto.ImageSearchHitDTO;
import com.example.imagesearchformal.domain.dto.ImageSearchQueryRequest;
import com.example.imagesearchformal.domain.dto.ImageSearchQueryResponse;
import com.example.imagesearchformal.domain.entity.*;
import com.example.imagesearchformal.domain.vo.ImageSearchQueryCmd;
import com.example.imagesearchformal.domain.vo.ImageSearchResultVO;
import com.example.imagesearchformal.mapper.ImgSearchInstanceMapper;
import com.example.imagesearchformal.mapper.ImgSearchQueryLogMapper;
import com.example.imagesearchformal.mapper.ImgSearchQueryResultMapper;
import com.example.imagesearchformal.service.ImageSearchImageService;
import com.example.imagesearchformal.service.ImageSearchQueryService;
import com.example.imagesearchformal.support.ImageSearchFilterBuilder;
import com.example.imagesearchformal.support.ImageSearchIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSearchQueryServiceImpl implements ImageSearchQueryService {

    private final ImgSearchInstanceMapper instanceMapper;
    private final ImageSearchClient imageSearchClient;
    private final ImageSearchImageService imageService;
    private final ImgSearchQueryLogMapper queryLogMapper;
    private final ImgSearchQueryResultMapper queryResultMapper;
    private final ImageSearchProperties properties;

    @Override
    public List<ImageSearchResultVO> search(ImageSearchQueryCmd cmd) {
        ImgSearchInstance instance = instanceMapper.selectEnabledInstance(properties.getProvider());
        if (instance == null) {
            throw new RuntimeException("未查询到启用中的图搜实例");
        }
        BigDecimal threshold = cmd.getScoreThreshold() == null
                ? BigDecimal.valueOf(properties.getDefaultScoreThreshold())
                : cmd.getScoreThreshold();
        Integer topK = cmd.getTopK() == null ? properties.getDefaultTopK() : cmd.getTopK();
        String filterExpr = ImageSearchFilterBuilder.build(cmd);

        ImageSearchQueryRequest request = new ImageSearchQueryRequest();
        request.setInstanceName(instance.getInstanceName());
        request.setImageUrl(cmd.getImageUrl());
        request.setNum(Math.min(topK, 100));
        request.setStart(0);
        request.setFilter(filterExpr);
        request.setScoreThreshold(threshold.toPlainString());
        request.setDistinctProductId(properties.getSearchDistinctProduct());
        request.setCrop(properties.getCrop());

        ImageSearchQueryResponse response = imageSearchClient.searchByPic(request);

        ImgSearchQueryLog log = new ImgSearchQueryLog();
        log.setQueryNo("TMP");
        log.setInstanceId(instance.getId());
        log.setBizScene("PRODUCT_CREATE");
        log.setQuerySource(2);
        log.setQueryImageUrl(cmd.getImageUrl());
        log.setThresholdScore(threshold);
        log.setTopK(topK);
        log.setFilterExpr(filterExpr);
        log.setDocsFound(response.getDocsFound());
        log.setDocsReturn(response.getDocsReturn());
        log.setSearchTimeMs(response.getSearchTime());
        log.setRequestId(response.getRequestId());
        log.setOperatorId(cmd.getOperatorId());
        log.setOperatorName(cmd.getOperatorName());
        if (response.getHits() != null && !response.getHits().isEmpty()) {
            ImageSearchHitDTO top1 = response.getHits().get(0);
            log.setTop1ProductId(top1.getProductId());
            log.setTop1PicName(top1.getPicName());
            log.setTop1Score(top1.getScore());
        }
        queryLogMapper.insert(log);
        log.setQueryNo(ImageSearchIdUtil.buildQueryNo(log.getId()));
        queryLogMapper.updateById(log);

        List<ImageSearchResultVO> resultList = new ArrayList<ImageSearchResultVO>();
        if (response.getHits() == null || response.getHits().isEmpty()) {
            return resultList;
        }
        int rank = 1;
        for (ImageSearchHitDTO hit : response.getHits()) {
            ImgSearchImage image = imageService.getByProductIdAndPicName(instance.getId(), hit.getProductId(), hit.getPicName());
            if (image == null) {
                continue;
            }
            ImageSearchResultVO vo = new ImageSearchResultVO();
            vo.setSku(image.getSku());
            vo.setImageId(image.getImageId());
            vo.setImageUrl(image.getImageUrl());
            vo.setScore(hit.getScore());
            vo.setCategoryId(image.getCategoryId());
            vo.setPlatformId(image.getPlatformId());
            vo.setSiteId(image.getSiteId());
            vo.setMainFlag(image.getMainFlag());
            resultList.add(vo);

            ImgSearchQueryResult item = new ImgSearchQueryResult();
            item.setQueryLogId(log.getId());
            item.setRankNo(rank++);
            item.setProductId(hit.getProductId());
            item.setPicName(hit.getPicName());
            item.setSku(image.getSku());
            item.setImageId(image.getImageId());
            item.setScore(hit.getScore());
            queryResultMapper.insert(item);
        }
        return resultList;
    }
}
