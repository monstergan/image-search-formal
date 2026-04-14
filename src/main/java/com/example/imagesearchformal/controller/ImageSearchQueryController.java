package com.example.imagesearchformal.controller;

import com.example.imagesearchformal.common.Result;
import com.example.imagesearchformal.domain.vo.ImageSearchQueryCmd;
import com.example.imagesearchformal.domain.vo.ImageSearchResultVO;
import com.example.imagesearchformal.service.ImageSearchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image-search")
@RequiredArgsConstructor
public class ImageSearchQueryController {

    private final ImageSearchQueryService imageSearchQueryService;

    @PostMapping("/query")
    public Result<List<ImageSearchResultVO>> query(@Validated @RequestBody ImageSearchQueryCmd cmd) {
        return Result.ok(imageSearchQueryService.search(cmd));
    }
}
