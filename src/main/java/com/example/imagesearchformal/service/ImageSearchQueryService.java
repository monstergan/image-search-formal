package com.example.imagesearchformal.service;

import com.example.imagesearchformal.domain.vo.ImageSearchQueryCmd;
import com.example.imagesearchformal.domain.vo.ImageSearchResultVO;

import java.util.List;

public interface ImageSearchQueryService {
    List<ImageSearchResultVO> search(ImageSearchQueryCmd cmd);
}
