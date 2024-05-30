package org.mrp.mrp.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Utils {
    private Utils() {
    }

    public static List<Long> parseStringIdStringToLongList(String ids) {
        List<Long> parsedIds = new ArrayList<>();
        for (String id : ids.split(",")) {
                parsedIds.add(Long.parseLong(id));
        }
        return parsedIds;
    }
}
