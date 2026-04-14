package com.example.imagesearchformal.support;

import java.time.LocalDateTime;

public final class RetryBackoffUtil {
    private RetryBackoffUtil() {
    }
    public static LocalDateTime nextRetryTime(int currentRetryCount) {
        switch (currentRetryCount) {
            case 0:
                return LocalDateTime.now().plusMinutes(5);
            case 1:
                return LocalDateTime.now().plusMinutes(15);
            case 2:
                return LocalDateTime.now().plusMinutes(30);
            case 3:
                return LocalDateTime.now().plusHours(2);
            default:
                return LocalDateTime.now().plusHours(6);
        }
    }
}
