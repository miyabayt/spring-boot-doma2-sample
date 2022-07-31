package com.sample.web.admin.controller.html.system.codes;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCriteria;
import com.sample.domain.service.system.CodeCategoryService;
import com.sample.domain.service.system.CodeService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** コード管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/system/codes")
@SessionAttributes(types = {SearchCodeForm.class, CodeForm.class})
@Slf4j
public class CodeHtmlController extends AbstractHtmlController {

  @NonNull final CodeFormValidator codeFormValidator;

  @NonNull final CodeService codeService;

  @NonNull final CodeCategoryService codeCategoryService;

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
  @PreAuthorize("hasAuthority('code:save')")
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
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('code:save')")
  @PostMapping("/new")
  public String newCode(
      @Validated @ModelAttribute("codeForm") CodeForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
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
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/find")
  public String findCode(
      @ModelAttribute("searchCodeForm") SearchCodeForm form, Pageable pageable, Model model) {
    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, CodeCriteria.class);

    // 10件区切りで取得する
    val pages = codeService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    // カテゴリ分類一覧
    val codeCategories = codeCategoryService.fetchAll();
    model.addAttribute("codeCategories", codeCategories);

    return "modules/system/codes/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('code:read')")
  @PostMapping("/find")
  public String findCode(
      @Validated @ModelAttribute("searchCodeForm") SearchCodeForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
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
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/show/{codeId}")
  public String showCode(@PathVariable Long codeId, Model model) {
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
  @PreAuthorize("hasAuthority('code:save')")
  @GetMapping("/edit/{codeId}")
  public String editCode(
      @PathVariable Long codeId, @ModelAttribute("codeForm") CodeForm form, Model model) {
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
   * @param br
   * @param codeId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('code:save')")
  @PostMapping("/edit/{codeId}")
  public String editCode(
      @Validated @ModelAttribute("codeForm") CodeForm form,
      BindingResult br,
      @PathVariable Long codeId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
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
  @PreAuthorize("hasAuthority('code:save')")
  @PostMapping("/remove/{codeId}")
  public String removeCode(@PathVariable Long codeId, RedirectAttributes attributes) {
    // 論理削除する
    codeService.delete(codeId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/system/codes/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public ModelAndView downloadCsv(@PathVariable String filename) {
    // 全件取得する
    val codes = codeService.findAll(new CodeCriteria(), Pageable.unpaged());

    // 詰め替える
    List<CodeCsv> csvList = modelMapper.map(codes.getContent(), toListType(CodeCsv.class));

    // CSVスキーマクラス、データ、ダウンロード時のファイル名を指定する
    val view = new CsvView(CodeCsv.class, csvList, filename);

    return new ModelAndView(view);
  }
}
