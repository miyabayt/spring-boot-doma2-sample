package com.sample.domain.dao;

import lombok.val;

import java.util.Objects;

/**
 * 二重送信防止チェックトークンホルダー
 */
public class DoubleSubmitCheckTokenHolder {

    private static final ThreadLocal<DoubleSubmitCheckTokenHolder> HOLDER = new ThreadLocal<>();
    private String expected;
    private String actual;
    private boolean excludeCheck;

    /**
     * トークンを保存します。
     *
     * @param expected
     * @param actual
     * @aparam excludeCheck
     */
    public static void set(String expected, String actual, boolean excludeCheck) {
        val me = new DoubleSubmitCheckTokenHolder();
        me.expected = expected;
        me.actual = actual;
        me.excludeCheck = excludeCheck;
        HOLDER.set(me);
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
