package com.sample.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * リストユーティリティ
 */
public class ListUtils {

    /**
     * リストのサイズを返します。
     * 
     * @param list
     * @return
     */
    public static int size(List<?> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /**
     * 引数の値をリストに追加します。
     * 
     * @param list
     * @param value
     * @return
     */
    public static <T> List<T> add(List<T> list, T value) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        return list;
    }
}
