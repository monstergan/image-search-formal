package com.example.imagesearchformal.service;

public interface ImageSearchRebuildService {
    Long createFullRebuildJob(Long instanceId);
    void executeFullRebuildJob(Long jobId);
}
