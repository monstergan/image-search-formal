package com.example.imagesearchformal.support;

import java.util.Set;

public final class ImageFileFilterUtil {

    private static final Set<String> IMAGE_EXT_SET = java.util.Collections.unmodifiableSet(
            new java.util.HashSet<String>(java.util.Arrays.asList("jpg", "jpeg", "png", "bmp", "gif", "webp", "tiff", "ppm"))
    );

    private ImageFileFilterUtil() {
    }

    public static boolean isImageFile(String fileName, String mimeType) {
        if (mimeType != null && mimeType.toLowerCase().startsWith("image/")) {
            return true;
        }
        if (fileName == null || !fileName.contains(".")) {
            return false;
        }
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return IMAGE_EXT_SET.contains(ext);
    }

    public static boolean isValidSize(Integer width, Integer height) {
        if (width == null || height == null) {
            return true;
        }
        return width >= 100 && width <= 4096 && height >= 100 && height <= 4096;
    }
}
