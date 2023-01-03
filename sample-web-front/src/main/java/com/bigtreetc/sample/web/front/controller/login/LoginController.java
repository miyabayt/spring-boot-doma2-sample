package com.bigtreetc.sample.web.front.controller.login;

import com.bigtreetc.sample.web.base.WebConst;
import com.bigtreetc.sample.web.base.controller.html.AbstractHtmlController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** ログイン */
@Controller
@Slf4j
public class LoginController extends AbstractHtmlController {

  @Override
  public String getFunctionName() {
    return "F_LOGIN";
  }

  /**
   * 初期表示
   *
   * @return
   */
  @GetMapping(WebConst.LOGIN_URL)
  public String index(@ModelAttribute LoginForm form) {
    return "login/login";
  }

  /**
   * 入力チェック
   *
   * @param form
   * @param br
   * @return
   */
  @PostMapping(WebConst.LOGIN_URL)
  public String index(@Validated @ModelAttribute LoginForm form, BindingResult br) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      return "login/login";
    }

    return "forward:" + WebConst.LOGIN_PROCESSING_URL;
  }

  /**
   * ログイン成功
   *
   * @param form
   * @param attributes
   * @return
   */
  @RequestMapping(WebConst.LOGIN_SUCCESS_URL)
  public String loginSuccess(@ModelAttribute LoginForm form, RedirectAttributes attributes) {
    attributes.addFlashAttribute(WebConst.SUCCESS_MESSAGE, getMessage("login.success"));
    return "redirect:/";
  }

  /**
   * ログイン失敗
   *
   * @param form
   * @param model
   * @return
   */
  @RequestMapping(WebConst.LOGIN_FAILURE_URL)
  public String loginFailure(@ModelAttribute LoginForm form, Model model) {
    model.addAttribute(WebConst.ERROR_MESSAGE, getMessage("login.failed"));
    return "login/login";
  }

  /**
   * ログアウト
   *
   * @return
   */
  @RequestMapping(WebConst.LOGOUT_URL)
  public String logout(@ModelAttribute LoginForm form, RedirectAttributes attributes) {
    attributes.addFlashAttribute(WebConst.SUCCESS_MESSAGE, getMessage("logout.success"));
    return "redirect:/login";
  }
}
