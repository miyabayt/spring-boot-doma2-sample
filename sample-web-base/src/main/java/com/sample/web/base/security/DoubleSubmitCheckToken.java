package com.sample.web.base.security;

import com.sample.common.XORShiftRandom;
import com.sample.web.base.util.SessionUtils;
import lombok.val;
import org.apache.commons.collections.map.LRUMap;

import javax.servlet.http.HttpServletRequest;

public class DoubleSubmitCheckToken {

    public static final String DOUBLE_SUBMIT_CHECK_PARAMETER = "_double";

    private static final String DOUBLE_SUBMIT_CHECK_CONTEXT = DoubleSubmitCheckToken.class.getName() + ".CONTEXT";

    // 乱数生成器
    private static final XORShiftRandom random = new XORShiftRandom();

    /**
     * 画面から渡ってきたトークンを返します。
     *
     * @param request
     * @return actual token
     */
    public static String getActualToken(HttpServletRequest request) {
        return request.getParameter(DOUBLE_SUBMIT_CHECK_PARAMETER);
    }

    /**
     * セッションに保存されているトークンを返します。
     *
     * @param request
     * @return expected token
     */
    @SuppressWarnings("unchecked")
    public static String getExpectedToken(HttpServletRequest request) {
        String token = null;
        val key = request.getRequestURI();

        Object mutex = SessionUtils.getMutex(request);
        if (mutex != null) {
            synchronized (mutex) {
                token = getToken(request, key);
            }
        }

        return token;
    }

    /**
     * セッションにトークンを設定します。
     *
     * @param request
     * @return token
     */
    @SuppressWarnings("unchecked")
    public static String renewToken(HttpServletRequest request) {
        val key = request.getRequestURI();
        val token = generateToken();

        Object mutex = SessionUtils.getMutex(request);
        synchronized (mutex) {
            setToken(request, key, token);
            return token;
        }
    }

    /**
     * トークンを生成します。
     *
     * @return token
     */
    public static String generateToken() {
        return String.valueOf(random.nextInt(Integer.MAX_VALUE));
    }

    /**
     * セッションに格納されたLRUMapを取り出す。存在しない場合は作成して返す。
     *
     * @param request
     * @return
     */
    protected static LRUMap getLRUMap(HttpServletRequest request) {
        LRUMap map = SessionUtils.getAttribute(request, DOUBLE_SUBMIT_CHECK_CONTEXT);

        if (map == null) {
            map = new LRUMap(10);
        }

        return map;
    }

    /**
     * トークンを取得する。
     *
     * @param request
     * @param key
     * @return
     */
    protected static String getToken(HttpServletRequest request, String key) {
        LRUMap map = getLRUMap(request);
        val token = (String) map.get(key);
        return token;
    }

    /**
     * トークンを保存する。
     *
     * @param request
     * @param key
     * @param token
     */
    protected static void setToken(HttpServletRequest request, String key, String token) {
        LRUMap map = getLRUMap(request);
        map.put(key, token);
        SessionUtils.setAttribute(request, DOUBLE_SUBMIT_CHECK_CONTEXT, map);
    }
}
