package com.sample.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * エンコードユーティリティ
 */
@Slf4j
public class EncodeUtils {

    /**
     * UTF-8でエンコードした文字列を返します。
     * 
     * @param filename
     * @return
     */
    public static String encodeUtf8(String filename) {
        String encoded = null;

        try {
            encoded = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            // should never happens
        }

        return encoded;
    }
}
