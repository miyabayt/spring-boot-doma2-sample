package com.sample.web.admin.controller.html.codes;

import java.util.List;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.domain.dto.Code;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.service.code.CodeService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * コード管理
 */
@Controller
@RequestMapping("/codes")
@SessionAttributes(types = { SearchCodeForm.class, CodeForm.class })
@Slf4j
public class CodeHtmlController extends AbstractHtmlController {

    @Autowired
    CodeFormValidator codeFormValidator;

    @Autowired
    CodeService codeService;

    @ModelAttribute("codeForm")
    public CodeForm codeForm() {
        return new CodeForm();
    }

    @ModelAttribute("searchCodeForm")
    public SearchCodeForm searchCodeForm() {
        return new SearchCodeForm();
    }

    @InitBinder("codeForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(codeFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_Code";
    }

    /**
     * 登録画面 初期表示
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newCode(@ModelAttribute("codeForm") CodeForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttributeに残っている場合は再生成する
            model.addAttribute("codeForm", new CodeForm());
        }

        // 登録処理の入力チェック結果を引き継ぐ
        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "codeForm";
            model.addAttribute(key, model.asMap().get("errors"));
        }

        return "codes/new";
    }

    /**
     * 登録処理
     *
     * @param form
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/new")
    public String newCode(@Validated @ModelAttribute("codeForm") CodeForm form, BindingResult result,
            RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/codes/new";
        }

        // 入力値からDTOを作成する
        val inputCode = modelMapper.map(form, Code.class);

        // 登録する
        val createdCode = codeService.create(inputCode);

        return "redirect:/codes/show/" + createdCode.getId().getValue();
    }

    /**
     * 一覧画面 初期表示
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findCode(@ModelAttribute("searchCodeForm") SearchCodeForm form, Model model) {
        // 入力値から検索条件を作成する
        val where = modelMapper.map(form, Code.class);

        // 10件区切りで取得する
        val pages = codeService.findAll(where, Pageable.DEFAULT_PAGEABLE);

        // 画面に検索結果を渡す
        model.addAttribute("pages", pages);

        return "codes/find";
    }

    /**
     * 検索結果
     *
     * @param form
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/find")
    public String findCode(@Validated @ModelAttribute("searchCodeForm") SearchCodeForm form, BindingResult result,
            RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/codes/find";
        }

        return "redirect:/codes/find";
    }

    /**
     * 詳細画面
     *
     * @param codeId
     * @param model
     * @return
     */
    @GetMapping("/show/{codeId}")
    public String showCode(@PathVariable Integer codeId, Model model) {
        // 1件取得する
        val code = codeService.findById(ID.of(codeId));
        model.addAttribute("code", code);
        return "codes/show";
    }

    /**
     * 編集画面 初期表示
     *
     * @param codeId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{codeId}")
    public String editCode(@PathVariable Integer codeId, @ModelAttribute("codeForm") CodeForm form, Model model) {
        // セッションから取得できる場合は、読み込み直さない
        if (form.getId() == null) {
            // 1件取得する
            val code = codeService.findById(ID.of(codeId));

            // 取得したDtoをFromに詰め替える
            modelMapper.map(code, form);
        }

        // 更新処理の入力チェック結果を引き継ぐ
        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "codeForm";
            model.addAttribute(key, model.asMap().get("errors"));
        }

        return "codes/new";
    }

    /**
     * 編集画面 更新処理
     *
     * @param form
     * @param result
     * @param codeId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{codeId}")
    public String editCode(@Validated @ModelAttribute("codeForm") CodeForm form, BindingResult result,
            @PathVariable Integer codeId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/codes/edit/" + codeId;
        }

        // 更新対象を取得する
        val code = codeService.findById(ID.of(codeId));

        // 入力値を詰め替える
        modelMapper.map(form, code);

        // 更新する
        val updatedCode = codeService.update(code);

        // セッションのcodeFormをクリアする
        sessionStatus.setComplete();

        return "redirect:/codes/show/" + updatedCode.getId().getValue();
    }

    /**
     * CSVダウンロード
     *
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+\\.csv}")
    public ModelAndView downloadCsv(@PathVariable String filename) {
        // 全件取得する
        val codes = codeService.findAll(new Code(), Pageable.NO_LIMIT_PAGEABLE);

        val listType = new TypeToken<List<CodeCsv>>() {
        }.getType();
        List<CodeCsv> csvList = modelMapper.map(codes.getData(), listType);

        // レスポンスを設定する
        val view = new CsvView(CodeCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
