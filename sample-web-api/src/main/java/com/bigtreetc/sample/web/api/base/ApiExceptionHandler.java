package com.bigtreetc.sample.web.api.base;

import static com.bigtreetc.sample.web.api.WebApiConst.ACCESS_DENIED_ERROR;
import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.common.util.MessageUtils;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import com.bigtreetc.sample.domain.exception.ValidationErrorException;
import com.bigtreetc.sample.web.api.base.resource.ErrorResourceImpl;
import com.bigtreetc.sample.web.api.base.resource.FieldErrorResource;
import java.util.ArrayList;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** API用の例外ハンドラー */
@RestControllerAdvice(annotations = RestController.class) // HTMLコントローラーの例外を除外する
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

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

      vee.getErrors()
          .ifPresent(
              errors -> {
                val fieldErrors = errors.getFieldErrors();

                if (fieldErrors != null) {
                  fieldErrors.forEach(
                      fieldError -> {
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
    val message = MessageUtils.getMessage(VALIDATION_ERROR, null, "validation error", locale);
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

    val message =
        MessageUtils.getMessage(NO_DATA_FOUND_ERROR, null, "no data found", request.getLocale());
    val errorResource = new ErrorResourceImpl();
    errorResource.setRequestId(String.valueOf(MDC.get("X-Track-Id")));
    errorResource.setMessage(message);
    errorResource.setFieldErrors(new ArrayList<>());

    return new ResponseEntity<>(errorResource, headers, status);
  }

  /**
   * 不正アクセスエラーのハンドリング
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
    val headers = new HttpHeaders();
    val status = HttpStatus.FORBIDDEN;
    val message =
        MessageUtils.getMessage(ACCESS_DENIED_ERROR, null, "access denied", request.getLocale());

    val response = new ErrorResourceImpl();
    response.setMessage(message);

    return new ResponseEntity<>(response, headers, status);
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
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      @Nullable Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    String parameterDump = this.dumpParameterMap(request.getParameterMap());
    log.error(String.format("unexpected error has occurred. dump: %s", parameterDump), ex);

    val locale = request.getLocale();
    val message = MessageUtils.getMessage(UNEXPECTED_ERROR, locale);
    val errorResource = new ErrorResourceImpl();
    errorResource.setRequestId(String.valueOf(MDC.get("X-Track-Id")));
    errorResource.setMessage(message);

    if (errorResource.getFieldErrors() == null) {
      errorResource.setFieldErrors(new ArrayList<>());
    }

    return new ResponseEntity<>(errorResource, headers, statusCode);
  }

  /**
   * パラメータをダンプする。
   *
   * @param parameterMap
   * @return
   */
  protected String dumpParameterMap(Map<String, String[]> parameterMap) {
    StringBuilder sb = new StringBuilder(256);
    parameterMap.forEach(
        (key, values) -> {
          sb.append(key).append("=").append("[");
          for (String value : values) {
            sb.append(value).append(",");
          }
          sb.delete(sb.length() - 1, sb.length()).append("], ");
        });
    int length = sb.length();
    if (2 <= length) sb.delete(length - 2, length);

    return sb.toString();
  }
}
