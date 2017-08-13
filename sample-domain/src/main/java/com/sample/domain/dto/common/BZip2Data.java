package com.sample.domain.dto.common;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.seasar.doma.Domain;

import com.sample.common.util.CompressUtils;

import lombok.NoArgsConstructor;
import lombok.val;

@Domain(valueType = byte[].class)
@NoArgsConstructor
public class BZip2Data implements Serializable {

    private static final long serialVersionUID = -4805556024192461766L;

    byte[] data;

    byte[] bzip2;

    /**
     * ファクトリメソッド
     * 
     * @param bytes
     * @return
     */
    public static BZip2Data of(byte[] bytes) {
        val bZip2Data = new BZip2Data();
        bZip2Data.data = bytes;
        return bZip2Data;
    }

    // ResultSet.getBytes(int)で取得された値がこのコンストラクタで設定される
    BZip2Data(byte[] bytes) {
        if (data == null) {
            data = CompressUtils.decompress(bytes);
        }
    }

    // PreparedStatement.setBytes(int, bytes)へ設定する値がこのメソッドから取得される
    byte[] getValue() {
        if (bzip2 == null) {
            bzip2 = CompressUtils.compress(data);
        }
        return bzip2;
    }

    public String toBase64() {
        return Base64.encodeBase64String(data);
    }
}
