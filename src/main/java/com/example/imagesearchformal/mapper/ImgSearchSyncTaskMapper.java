package com.example.imagesearchformal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.imagesearchformal.domain.entity.ImgSearchSyncTask;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ImgSearchSyncTaskMapper extends BaseMapper<ImgSearchSyncTask> {
    int markRunning(@Param("taskId") Long taskId);
    int markSuccess(@Param("taskId") Long taskId, @Param("requestId") String requestId);
    int markRetry(@Param("taskId") Long taskId, @Param("errorCode") String errorCode,
                  @Param("errorMsg") String errorMsg, @Param("nextRetryTime") LocalDateTime nextRetryTime);
    int markDead(@Param("taskId") Long taskId, @Param("errorCode") String errorCode, @Param("errorMsg") String errorMsg);
    int markCanceled(@Param("taskId") Long taskId, @Param("reason") String reason);
    List<ImgSearchSyncTask> selectRetryableTaskList(@Param("now") LocalDateTime now, @Param("limitSize") Integer limitSize);
}
