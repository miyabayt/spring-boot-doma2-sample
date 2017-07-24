package com.sample.web.admin.controller.html.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sample.web.base.controller.html.AbstractHtmlController;

import lombok.extern.slf4j.Slf4j;

/**
 * ホーム
 */
@Controller
@RequestMapping(path = { "/", "/home" })
@Slf4j
public class HomeHtmlController extends AbstractHtmlController {

    @Override
    public String getFunctionName() {
        return "A_Home";
    }

    /**
     * 初期表示
     *
     * @return
     */
    @GetMapping
    public String index(Model model) {
        return "home/index";
    }
}
