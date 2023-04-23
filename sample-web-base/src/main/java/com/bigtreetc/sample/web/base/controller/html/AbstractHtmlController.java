package com.bigtreetc.sample.web.base.controller.html;

import static com.bigtreetc.sample.web.base.WebConst.MAV_ERRORS;

import com.bigtreetc.sample.common.FunctionNameAware;
import com.bigtreetc.sample.web.base.controller.BaseController;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 基底HTMLコントローラー */
@Slf4j
public abstract class AbstractHtmlController extends BaseController implements FunctionNameAware {

  /**
   * 入力チェックエラーがある場合はtrueを返します。
   *
   * @param model
   * @return
   */
  public boolean hasErrors(Model model) {
    val errors = model.asMap().get(MAV_ERRORS);

    if (errors instanceof BeanPropertyBindingResult br) {
      return br.hasErrors();
    }

    return false;
  }

  /**
   * リダイレクト先に入力エラーを渡します。
   *
   * @param attributes
   * @param result
   */
  public void setFlashAttributeErrors(RedirectAttributes attributes, BindingResult result) {
    val fieldErrors =
        result.getFieldErrors().stream()
            .map(
                fieldError -> {
                  val rejectedValue = fieldError.getRejectedValue();
                  if (rejectedValue instanceof MultipartFile) {
                    val objectName = fieldError.getObjectName();
                    val field = fieldError.getField();
                    val defaultMessage = fieldError.getDefaultMessage();
                    return new FieldError(objectName, field, defaultMessage);
                  }
                  return fieldError;
                })
            .toList();

    val target = result.getTarget();
    val objectName = result.getObjectName();
    val br = new BeanPropertyBindingResult(target, objectName);

    for (val fieldError : fieldErrors) {
      br.addError(fieldError);
    }

    attributes.addFlashAttribute(MAV_ERRORS, br);
  }

  @SneakyThrows
  protected void setContentDispositionHeader(
      HttpServletResponse response, String filename, boolean isAttachment) {
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

    if (isAttachment) {
      val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
      val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
    }
  }
}
