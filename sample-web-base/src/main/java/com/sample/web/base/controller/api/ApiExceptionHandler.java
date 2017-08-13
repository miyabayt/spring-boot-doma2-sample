package com.sample.web.base.controller.api;

import static com.sample.web.base.WebConst.VALIDATION_ERROR;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sample.domain.exception.NoDataFoundException;
import com.sample.web.base.controller.api.resource.ErrorResourceImpl;
import com.sample.web.base.controller.api.resource.FieldErrorResource;
import com.sample.web.base.exception.ValidationErrorException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * API用の例外ハンドラー
 */
@RestControllerAdvice(annotations = RestController.class) // HTMLコントローラーの例外を除外する
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    MessageSource messageSource;

    /**
     * 入力チェックエラーのハンドリング
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<Object> handleValidationErrorException(Exception ex, WebRequest request) {
        val headers = new HttpHeaders();
        val status = HttpStatus.BAD_REQUEST;
        val fieldErrorContexts = new ArrayList<FieldErrorResource>();

        if (ex instanceof ValidationErrorException) {
            val vee = (ValidationErrorException) ex;

            vee.getErrors().ifPresent(errors -> {
                val fieldErrors = errors.getFieldErrors();

                if (fieldErrors != null) {
                    fieldErrors.forEach(fieldError -> {
                        val fieldErrorResource = new FieldErrorResource();
                        fieldErrorResource.setFieldName(fieldError.getField());
                        fieldErrorResource.setErrorType(fieldError.getCode());
                        fieldErrorResource.setErrorMessage(fieldError.getDefaultMessage());
                        fieldErrorContexts.add(fieldErrorResource);
                    });
                }
            });
        }

        val locale = request.getLocale();
        val message = messageSource.getMessage(VALIDATION_ERROR, null, "validation error", locale);
        val errorContext = new ErrorResourceImpl();
        errorContext.setMessage(message);
        errorContext.setFieldErrors(fieldErrorContexts);

        return new ResponseEntity<>(errorContext, headers, status);
    }

    /**
     * データ不存在エラーのハンドリング
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(Exception ex, WebRequest request) {
        val headers = new HttpHeaders();
        val status = HttpStatus.OK;

        String parameterDump = this.dumpParameterMap(request.getParameterMap());
        log.info("no data found. dump: {}", parameterDump);

        val message = messageSource.getMessage("NoDataFound.message", null, "no data found", request.getLocale());
        val errorResource = new ErrorResourceImpl();
        errorResource.setRequestId(String.valueOf(MDC.get("X-Track-Id")));
        errorResource.setMessage(message);
        errorResource.setFieldErrors(new ArrayList<>());

        return new ResponseEntity<>(errorResource, headers, status);
    }

    /**
     * 予期せぬ例外のハンドリング
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String parameterDump = this.dumpParameterMap(request.getParameterMap());
        log.error(String.format("unexpected error has occurred. dump: %s", parameterDump), ex);

        val locale = request.getLocale();
        val message = messageSource.getMessage("UnexpectedError.message", null, "unexpected error", locale);
        val errorResource = new ErrorResourceImpl();
        errorResource.setRequestId(String.valueOf(MDC.get("X-Track-Id")));
        errorResource.setMessage(message);

        if (errorResource.getFieldErrors() == null) {
            errorResource.setFieldErrors(new ArrayList<>());
        }

        return new ResponseEntity<>(errorResource, headers, status);
    }

    /**
     * パラメータをダンプする。
     *
     * @param parameterMap
     * @return
     */
    protected String dumpParameterMap(Map<String, String[]> parameterMap) {
        StringBuilder sb = new StringBuilder(256);
        parameterMap.forEach((key, values) -> {
            sb.append(key).append("=").append("[");
            for (String value : values) {
                sb.append(value).append(",");
            }
            sb.delete(sb.length() - 1, sb.length()).append("], ");
        });
        int length = sb.length();
        if (2 <= length)
            sb.delete(length - 2, length);

        return sb.toString();
    }
}
