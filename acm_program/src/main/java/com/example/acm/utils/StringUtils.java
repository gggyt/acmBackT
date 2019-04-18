package com.example.acm.utils;

/**
 * Created by xgg on 2018/11/25.
 */
public class StringUtils {

    public static boolean isNull(String s) {
        if (s == null || "".equals(s)) {
            return true;
        }
        return false;
    }
}
