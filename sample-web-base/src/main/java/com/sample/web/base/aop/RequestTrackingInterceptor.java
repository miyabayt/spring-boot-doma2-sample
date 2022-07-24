package com.sample.web.base.aop;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import com.sample.common.XORShiftRandom;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;

/** 処理時間をDEBUGログに出力する */
@Slf4j
public class RequestTrackingInterceptor extends BaseHandlerInterceptor {

  private static final AntPathMatcher pathMatcher = new AntPathMatcher();

  private static final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

  private static final String HEADER_X_TRACK_ID = "X-Track-Id";

  // 乱数生成器
  private final XORShiftRandom random = new XORShiftRandom();

  private final String[] excludeUrlPatterns;

  public RequestTrackingInterceptor(String[] excludeUrlPatterns) {
    this.excludeUrlPatterns = excludeUrlPatterns;
  }

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
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // 処理完了後
    val beforeNanoSec = startTimeHolder.get();
    if (beforeNanoSec == null) {
      return;
    }
    if (shouldNotFilter(request)) {
      return;
    }

    val elapsedNanoSec = System.nanoTime() - beforeNanoSec;
    val elapsedMilliSec = NANOSECONDS.toMillis(elapsedNanoSec);
    log.info(
        "path={}, method={}, Elapsed {}ms.",
        request.getRequestURI(),
        request.getMethod(),
        elapsedMilliSec);

    // 破棄する
    startTimeHolder.remove();
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

  protected boolean shouldNotFilter(HttpServletRequest request) {
    for (val excludeUrlPattern : excludeUrlPatterns) {
      if (pathMatcher.match(excludeUrlPattern, request.getServletPath())) {
        return true;
      }
    }

    return false;
  }
}
