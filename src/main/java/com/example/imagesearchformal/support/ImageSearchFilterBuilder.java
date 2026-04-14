package com.example.imagesearchformal.support;

import com.example.imagesearchformal.domain.vo.ImageSearchQueryCmd;

import java.util.ArrayList;
import java.util.List;

public final class ImageSearchFilterBuilder {
    private ImageSearchFilterBuilder() {
    }
    public static String build(ImageSearchQueryCmd cmd) {
        List<String> conditions = new ArrayList<String>();
        if (cmd.getMainFlag() != null) {
            conditions.add("int_attr=" + (Boolean.TRUE.equals(cmd.getMainFlag()) ? 1 : 0));
        }
        if (cmd.getPlatformId() != null) {
            conditions.add("int_attr2=" + cmd.getPlatformId());
        }
        if (cmd.getSiteId() != null) {
            conditions.add("int_attr3=" + cmd.getSiteId());
        }
        if (cmd.getCategoryId() != null) {
            conditions.add("int_attr4=" + cmd.getCategoryId());
        }
        return String.join(" AND ", conditions);
    }
}
