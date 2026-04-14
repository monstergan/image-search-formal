package com.example.imagesearchformal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.imagesearchformal.domain.entity.ImgSearchImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImgSearchImageMapper extends BaseMapper<ImgSearchImage> {
    ImgSearchImage selectByInstanceIdAndImageId(@Param("instanceId") Long instanceId,
                                                @Param("imageId") Long imageId);
    ImgSearchImage selectByProductIdAndPicName(@Param("instanceId") Long instanceId,
                                               @Param("productId") String productId,
                                               @Param("picName") String picName);
    List<ImgSearchImage> selectPageForRebuild(@Param("instanceId") Long instanceId,
                                              @Param("lastId") Long lastId,
                                              @Param("snapshotMaxId") Long snapshotMaxId,
                                              @Param("limitSize") Integer limitSize);
    int updateSyncing(@Param("id") Long id);
    int updateSyncSuccess(@Param("id") Long id, @Param("requestId") String requestId);
    int updateSyncDeleted(@Param("id") Long id, @Param("requestId") String requestId);
    int updateSyncFail(@Param("id") Long id, @Param("errorCode") String errorCode, @Param("errorMsg") String errorMsg);
}
