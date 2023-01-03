package com.bigtreetc.sample.web.admin.controller.home;

import static com.bigtreetc.sample.web.base.WebConst.HOME_URL;

import com.bigtreetc.sample.web.base.controller.html.AbstractHtmlController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** ホーム */
@Controller
@RequestMapping(path = {HOME_URL, "/home"})
@Slf4j
public class HomeController extends AbstractHtmlController {

  @Override
  public String getFunctionName() {
    return "A_HOME";
  }

  /**
   * 初期表示
   *
   * @return
   */
  @PreAuthorize("hasAnyRole('system_admin', 'operator')")
  @GetMapping
  public String index(Model model) {
    return "modules/home/index";
  }
}
