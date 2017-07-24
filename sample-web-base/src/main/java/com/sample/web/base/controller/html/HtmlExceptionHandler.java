package com.sample.web.base.controller.html;

import static com.sample.web.base.WebConst.*;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.sample.domain.exception.FileNotFoundException;
import com.sample.domain.exception.NoDataFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * 画面機能用の例外ハンドラー
 */
@ControllerAdvice(assignableTypes = { AbstractHtmlController.class }) // RestControllerでは動作させない
@Slf4j
public class HtmlExceptionHandler {

    @Autowired
    protected MessageSource messageSource;

    /**
     * ファイル、データ不存在時の例外をハンドリングする
     *
     * @param e
     * @return
     */
    @ExceptionHandler({ FileNotFoundException.class, NoDataFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception e) {
        if (log.isDebugEnabled()) {
            log.debug("not found.", e);
        }

        return "redirect:" + NOTFOUND_URL;
    }

    /**
     * 楽観的排他制御により発生する例外をハンドリングする
     * 
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ OptimisticLockingFailureException.class, InvalidCsrfTokenException.class })
    public RedirectView handleOptimisticLockingFailureException(Exception e, HttpServletRequest request,
            HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("optimistic locking failure.", e);
        }

        // 共通メッセージを取得する
        Locale locale = RequestContextUtils.getLocale(request);
        String messageCode = null;
        if (e instanceof OptimisticLockingFailureException) {
            messageCode = OPTIMISTIC_LOCKING_FAILURE_ERROR;
        } else if (e instanceof InvalidCsrfTokenException) {
            messageCode = INVALID_CSRF_TOKEN_ERROR;
        }

        // 排他エラーのメッセージを遷移先に表示する
        String message = messageSource.getMessage(messageCode, null, locale);
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(GLOBAL_MESSAGE, message);

        // flashMapを書き込む
        RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);

        String requestURI = request.getRequestURI();
        RedirectView redirectView = new RedirectView(requestURI);

        return redirectView;
    }

    /**
     * 予期せぬ例外をハンドリングする
     *
     * @param t
     * @return
     */
    @ExceptionHandler({ Throwable.class })
    public String handleException(Throwable t) {

        // TODO
        // ハンドルする例外がある場合は、条件分岐する

        log.error("unhandled runtime exception.", t);

        return "redirect:" + ERROR_URL;
    }
}
