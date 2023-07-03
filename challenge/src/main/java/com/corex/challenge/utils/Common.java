package com.corex.challenge.utils;

public class Common {

    public static String getRelativeUrl(String absoluteUrl) {
        String[] parts = absoluteUrl.split("/");
        return "/" + parts[parts.length - 2] + "/" + parts[parts.length - 1];
    }
}
