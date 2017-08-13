package com.sample.domain.dto.common;

import java.io.Serializable;
import java.util.Objects;

import org.seasar.doma.Domain;

/**
 * ID値を内包するクラス
 * 
 * @param <T>
 */
@Domain(valueType = Integer.class, factoryMethod = "of")
public class ID<T> implements Serializable {

    private static final long serialVersionUID = -8883289947172519834L;

    private static final ID<Object> NOT_ASSIGNED = new ID<>(null);

    private final Integer value;

    /**
     * コンストラクタ
     * 
     * @param value
     */
    private ID(final Integer value) {
        this.value = value;
    }

    /**
     * 内包している値を返します。
     * 
     * @return
     */
    public Integer getValue() {
        return value;
    }

    /**
     * ファクトリメソッド
     * 
     * @param value
     * @param <R>
     * @return
     */
    public static <R> ID<R> of(final Integer value) {
        return new ID<>(value);
    }

    /**
     * 採番されていない状態のIDを返します。
     *
     * @param <R>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <R> ID<R> notAssigned() {
        return (ID<R>) NOT_ASSIGNED;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (Objects.equals(this, NOT_ASSIGNED)) {
            // NOT_ASSINED同士の比較もfalseを返す
            return false;
        }

        if (Objects.equals(this, o)) {
            return true;
        }

        final ID<?> id = (ID<?>) o;
        return value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
