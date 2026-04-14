package com.example.imagesearchformal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.imagesearchformal.domain.entity.ImgSearchRebuildJob;
import org.apache.ibatis.annotations.Param;

public interface ImgSearchRebuildJobMapper extends BaseMapper<ImgSearchRebuildJob> {
    int markRunning(@Param("jobId") Long jobId);
    int addSuccessCount(@Param("jobId") Long jobId, @Param("count") int count);
    int addFailCount(@Param("jobId") Long jobId, @Param("count") int count);
    int markFinished(@Param("jobId") Long jobId);
    int markStopped(@Param("jobId") Long jobId, @Param("remark") String remark);
}
