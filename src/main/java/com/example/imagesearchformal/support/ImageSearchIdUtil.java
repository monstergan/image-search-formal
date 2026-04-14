package com.example.imagesearchformal.support;

public final class ImageSearchIdUtil {
    private ImageSearchIdUtil() {
    }
    public static String buildProductId(String sku) {
        return sku;
    }
    public static String buildPicName(Long imageId) {
        return String.valueOf(imageId);
    }
    public static int bucketNo(Long imageId) {
        return (int) (Math.abs(imageId) % 64);
    }
    public static String buildTaskNo(Long taskId) {
        return "IMG_TASK_" + taskId;
    }
    public static String buildQueryNo(Long queryId) {
        return "IMG_QUERY_" + queryId;
    }
    public static String buildRebuildJobNo(Long jobId) {
        return "IMG_REBUILD_" + jobId;
    }
}
