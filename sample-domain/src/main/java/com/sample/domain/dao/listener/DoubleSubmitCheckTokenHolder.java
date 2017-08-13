package com.sample.domain.dao.listener;

/**
 * 二重送信防止チェックトークンホルダー
 */
public class DoubleSubmitCheckTokenHolder {

    private static final ThreadLocal<String> EXPECTED_TOKEN = new ThreadLocal<>();

    private static final ThreadLocal<String> ACTUAL_TOKEN = new ThreadLocal<>();

    /**
     * トークンを保存します。
     *
     * @param expected
     * @param actual
     */
    public static void set(String expected, String actual) {
        EXPECTED_TOKEN.set(expected);
        ACTUAL_TOKEN.set(actual);
    }

    /**
     * セッションに保存されていたトークンを返します。
     *
     * @return
     */
    public static String getExpectedToken() {
        return EXPECTED_TOKEN.get();
    }

    /**
     * 画面から渡ってきたトークンを返します。
     *
     * @return
     */
    public static String getActualToken() {
        return ACTUAL_TOKEN.get();
    }

    /**
     * 監査情報をクリアします。
     */
    public static void clear() {
        EXPECTED_TOKEN.remove();
        ACTUAL_TOKEN.remove();
    }
}
