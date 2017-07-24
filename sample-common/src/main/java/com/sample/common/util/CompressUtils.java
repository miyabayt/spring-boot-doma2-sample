package com.sample.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 圧縮ユーティリティ
 */
@Slf4j
public class CompressUtils {

    /**
     * 入力したバイト配列をBZip2で圧縮して返します。
     * 
     * @param input
     * @return
     */
    public static byte[] compress(byte[] input) {
        ByteArrayOutputStream ref = null;

        try (val bais = new ByteArrayInputStream(input);
                val baos = new ByteArrayOutputStream(input.length);
                val bzip2cos = new BZip2CompressorOutputStream(baos)) {
            IOUtils.copy(bais, bzip2cos);
            ref = baos;
        } catch (IOException e) {
            log.error("failed to encode.", e);
            throw new RuntimeException(e);
        }

        return ref.toByteArray();
    }

    /**
     * 入力したバイト配列をBZip2で展開して返します。
     * 
     * @param input
     * @return
     */
    public static byte[] decompress(byte[] input) {
        ByteArrayOutputStream ref = null;

        try (val bais = new ByteArrayInputStream(input);
                val bzip2cis = new BZip2CompressorInputStream(bais);
                val baos = new ByteArrayOutputStream()) {
            IOUtils.copy(bzip2cis, baos);
            ref = baos;
        } catch (IOException e) {
            log.error("failed to decode.", e);
            throw new RuntimeException(e);
        }

        return ref.toByteArray();
    }
}
