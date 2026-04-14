package com.example.imagesearchformal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.imagesearchformal.domain.entity.ImgSearchInstance;
import org.apache.ibatis.annotations.Param;

public interface ImgSearchInstanceMapper extends BaseMapper<ImgSearchInstance> {
    ImgSearchInstance selectEnabledInstance(@Param("provider") String provider);
}
