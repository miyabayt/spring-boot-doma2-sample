package com.sample.web.base.controller.html;

import static com.sample.web.base.WebConst.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.sample.common.util.MessageUtils;
import com.sample.domain.exception.DoubleSubmitErrorException;
import com.sample.domain.exception.FileNotFoundException;
import com.sample.domain.exception.NoDataFoundException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 画面機能用の例外ハンドラー
 */
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
@Slf4j
public class HtmlExceptionHandler {

    private static final String VIEW_ATTR_STACKTRACE = "trace";

    /**
     * ファイル、データ不存在時の例外をハンドリングする
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ FileNotFoundException.class, NoDataFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("not found.", e);
        }

        val stackTrace = getStackTraceAsString(e);
        outputFlashMap(request, response, VIEW_ATTR_STACKTRACE, stackTrace);

        return "redirect:" + NOTFOUND_URL;
    }

    /**
     * 権限不足エラーの例外をハンドリングする
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("forbidden.", e);
        }

        val stackTrace = getStackTraceAsString(e);
        outputFlashMap(request, response, VIEW_ATTR_STACKTRACE, stackTrace);

        return "redirect:" + FORBIDDEN_URL;
    }

    /**
     * 楽観的排他制御により発生する例外をハンドリングする
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ OptimisticLockingFailureException.class })
    public RedirectView handleOptimisticLockingFailureException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("optimistic locking failure.", e);
        }

        // 共通メッセージを取得する
        val locale = RequestContextUtils.getLocale(request);
        val messageCode = OPTIMISTIC_LOCKING_FAILURE_ERROR;
        val view = getRedirectView(request, response, locale, messageCode);

        return view;
    }

    /**
     * 二重送信防止チェックにより発生する例外をハンドリングする
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ DoubleSubmitErrorException.class })
    public RedirectView handleDoubleSubmitErrorException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("double submit error.");
        }

        // 共通メッセージを取得する
        val locale = RequestContextUtils.getLocale(request);
        val messageCode = DOUBLE_SUBMIT_ERROR;
        val view = getRedirectView(request, response, locale, messageCode);

        return view;
    }

    /**
     * 予期せぬ例外をハンドリングする
     * 
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public String handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        // TODO
        // ハンドルする例外がある場合は、条件分岐する
        log.error("unhandled runtime exception.", e);

        val stackTrace = getStackTraceAsString(e);
        outputFlashMap(request, response, VIEW_ATTR_STACKTRACE, stackTrace);

        return "redirect:" + ERROR_URL;
    }

    /**
     * リダイレクト先でグローバルメッセージを表示する
     *
     * @param request
     * @param response
     * @param locale
     * @param messageCode
     * @return
     */
    protected RedirectView getRedirectView(HttpServletRequest request, HttpServletResponse response, Locale locale,
            String messageCode) {
        // メッセージを遷移先に表示する
        val message = MessageUtils.getMessage(messageCode, locale);
        outputFlashMap(request, response, GLOBAL_MESSAGE, message);

        val requestURI = request.getRequestURI();
        val view = new RedirectView(requestURI);

        return view;
    }

    /**
     * スタックトレースを文字列で返します。
     * 
     * @param e
     * @return
     */
    protected String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /**
     * FlashMapに値を書き出します。
     * 
     * @param request
     * @param response
     * @param attr
     * @param value
     */
    protected void outputFlashMap(HttpServletRequest request, HttpServletResponse response, String attr, String value) {
        val flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(attr, value);

        // flashMapを書き込む
        val flashManager = RequestContextUtils.getFlashMapManager(request);
        flashManager.saveOutputFlashMap(flashMap, request, response);
    }
}
