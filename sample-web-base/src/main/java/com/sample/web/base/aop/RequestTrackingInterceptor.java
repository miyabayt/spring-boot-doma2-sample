package com.sample.web.base.aop;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.common.XORShiftRandom;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 処理時間をDEBUGログに出力する
 */
@Slf4j
public class RequestTrackingInterceptor extends HandlerInterceptorAdapter {

    private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

    private static final String HEADER_X_TRACK_ID = "X-Track-Id";

    // 乱数生成器
    private final XORShiftRandom random = new XORShiftRandom();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前

        // 現在時刻を記録
        val beforeNanoSec = System.nanoTime();
        startTimeHolder.set(beforeNanoSec);

        // トラッキングID
        val trackId = getTrackId(request);
        MDC.put(HEADER_X_TRACK_ID, trackId);
        response.setHeader(HEADER_X_TRACK_ID, trackId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後

        val beforeNanoSec = startTimeHolder.get();
        val elapsedNanoSec = System.nanoTime() - beforeNanoSec;
        val elapsedMilliSec = NANOSECONDS.toMillis(elapsedNanoSec);
        log.info("path={}, method={}, Elapsed {}ms.", request.getRequestURI(), request.getMethod(), elapsedMilliSec);

        // 破棄する
        startTimeHolder.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
    }

    /**
     * トラッキングIDを取得する。
     * 
     * @param request
     * @return
     */
    private String getTrackId(HttpServletRequest request) {
        String trackId = request.getHeader(HEADER_X_TRACK_ID);
        if (trackId == null) {
            int seed = Integer.MAX_VALUE;
            trackId = String.valueOf(random.nextInt(seed));
        }

        return trackId;
    }
}
