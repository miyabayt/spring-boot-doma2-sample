package com.sample.web.admin.controller.html.system.codes;

import static com.sample.domain.util.TypeUtils.toListType;

import java.util.List;

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

import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Code;
import com.sample.domain.service.system.CodeService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * コード管理
 */
@Controller
@RequestMapping("/system/codes")
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
        return "A_CODE";
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

        return "modules/system/codes/new";
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
            setFlashAttributeErrors(attributes, result);
            return "redirect:/system/codes/new";
        }

        // 入力値からDTOを作成する
        val inputCode = modelMapper.map(form, Code.class);

        // 登録する
        val createdCode = codeService.create(inputCode);

        return "redirect:/system/codes/show/" + createdCode.getId();
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
        val pages = codeService.findAll(where, form);

        // 画面に検索結果を渡す
        model.addAttribute("pages", pages);

        return "modules/system/codes/find";
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
            setFlashAttributeErrors(attributes, result);
            return "redirect:/system/codes/find";
        }

        return "redirect:/system/codes/find";
    }

    /**
     * 詳細画面
     *
     * @param codeId
     * @param model
     * @return
     */
    @GetMapping("/show/{codeId}")
    public String showCode(@PathVariable Long codeId, Model model) {
        // 1件取得する
        val code = codeService.findById(codeId);
        model.addAttribute("code", code);
        return "modules/system/codes/show";
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
    public String editCode(@PathVariable Long codeId, @ModelAttribute("codeForm") CodeForm form, Model model) {
        // セッションから取得できる場合は、読み込み直さない
        if (!hasErrors(model)) {
            // 1件取得する
            val code = codeService.findById(codeId);

            // 取得したDtoをFromに詰め替える
            modelMapper.map(code, form);
        }

        return "modules/system/codes/new";
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
            @PathVariable Long codeId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            setFlashAttributeErrors(attributes, result);
            return "redirect:/system/codes/edit/" + codeId;
        }

        // 更新対象を取得する
        val code = codeService.findById(codeId);

        // 入力値を詰め替える
        modelMapper.map(form, code);

        // 更新する
        val updatedCode = codeService.update(code);

        // セッションのcodeFormをクリアする
        sessionStatus.setComplete();

        return "redirect:/system/codes/show/" + updatedCode.getId();
    }

    /**
     * 削除処理
     *
     * @param codeId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{codeId}")
    public String removeCode(@PathVariable Long codeId, RedirectAttributes attributes) {
        throw new UnsupportedOperationException();
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

        // 詰め替える
        List<CodeCsv> csvList = modelMapper.map(codes.getData(), toListType(CodeCsv.class));

        // レスポンスを設定する
        val view = new CsvView(CodeCsv.class, csvList);
        view.setFilename(filename);

        return new ModelAndView(view);
    }
}
