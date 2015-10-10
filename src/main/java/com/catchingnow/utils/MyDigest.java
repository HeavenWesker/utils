package com.catchingnow.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Heaven on 9/13/15.
 */
public class MyDigest {
    public static String MD5(String source) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = digest.digest(source.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            builder.append(Integer.toHexString((aByte & 0xFF) | 0x100).substring(1, 3));
        }
        return builder.toString();
    }
}
