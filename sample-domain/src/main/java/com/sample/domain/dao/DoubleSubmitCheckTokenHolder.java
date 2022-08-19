package com.sample.domain.dao;

import lombok.val;

import java.util.Objects;

/**
 * 二重送信防止チェックトークンホルダー
 */
public class DoubleSubmitCheckTokenHolder {

    private static final ThreadLocal<DoubleSubmitCheckTokenHolder> HOLDER = new ThreadLocal<>();
    private String key;
    private String expected;
    private String actual;
    private boolean existsExpectedTokenKey;
    private boolean excludeCheck;

    /**
     * トークンを保存します。
     *
     * @param key
     * @param expected
     * @param actual
     * @param existsExpectedTokenKey
     * @aparam excludeCheck
     */
    public static void set(String key, String expected, String actual, boolean existsExpectedTokenKey, boolean excludeCheck) {
        val me = new DoubleSubmitCheckTokenHolder();
        me.key = key;
        me.expected = expected;
        me.actual = actual;
        me.existsExpectedTokenKey = existsExpectedTokenKey;
        me.excludeCheck = excludeCheck;
        HOLDER.set(me);
    }

    /**
     * トークンのキーを返します。
     *
     * @return
     */
    public static String getTokenKey() {
        return me().key;
    }

    /**
     * セッションに保存されていたトークンを返します。
     *
     * @return
     */
    public static String getExpectedToken() {
        return me().expected;
    }

    /**
     * 画面から渡ってきたトークンを返します。
     *
     * @return
     */
    public static String getActualToken() {
        return me().actual;
    }

    /**
     * セッション中にトークンキーがあるかを返します
     *
     * @return
     */
    public static boolean isExistsExpectedTokenKey(){
        return me().existsExpectedTokenKey;
    }

    /**
     * トークンチェックの対象から除外するかを返します。
     *
     * @return
     */
    public static boolean isExcludeCheck() {
        return me().excludeCheck;
    }

    /**
     * 監査情報をクリアします。
     */
    public static void clear() {
        HOLDER.remove();
    }

    private static DoubleSubmitCheckTokenHolder me(){
        if( Objects.isNull(HOLDER.get()) ){
            return new DoubleSubmitCheckTokenHolder();
        }
        return HOLDER.get();
    }
}
