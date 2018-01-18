package com.sample.domain.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Typeを扱うユーティリティ
 */
public class TypeUtils {

    /**
     * Typeを返します。
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Type toListType(Class<T> clazz) {
        return new ListParameterizedType(clazz);
    }

    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] { type };
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
