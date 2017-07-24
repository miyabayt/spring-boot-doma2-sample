package com.sample.web.front.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sample.web.base.controller.html.AbstractHtmlController;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(path = { "/", "/home" })
@Slf4j
public class HomeController extends AbstractHtmlController {

    @Override
    public String getFunctionName() {
        return "F_Home";
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
