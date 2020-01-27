package com.majoapps.dogbreed.utility;

public class UrlParser { 

    private final static String START_STRING = "breeds/";
    private final static String END_STRING = "/";

    public static String dogNameFromUrl(String urlString){
        if (urlString.contains(START_STRING)) {
            int startPos = urlString.indexOf(START_STRING, 0) + START_STRING.length();
            int endPos = urlString.indexOf(END_STRING, startPos);
            if (startPos >= 0 && endPos >= 0 && (endPos > startPos)) {
                return (urlString.substring(startPos, endPos));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}