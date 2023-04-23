package com.bigtreetc.sample.web.admin.controller.codecategory;

import static com.bigtreetc.sample.common.util.ValidateUtils.isTrue;
import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.domain.entity.CodeCategory;
import com.bigtreetc.sample.domain.entity.CodeCategoryCriteria;
import com.bigtreetc.sample.domain.service.codecategory.CodeCategoryService;
import com.bigtreetc.sample.web.base.controller.html.AbstractHtmlController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** コード分類マスタ */
@RequiredArgsConstructor
@Controller
@RequestMapping("/codeCategories")
@SessionAttributes(types = {SearchCodeCategoryForm.class, CodeCategoryForm.class})
@Slf4j
public class CodeCategoryController extends AbstractHtmlController {

  @NonNull final CodeCategoryFormValidator codeCategoryFormValidator;

  @NonNull final CodeCategoryService codeCategoryService;

  @ModelAttribute("codeCategoryForm")
  public CodeCategoryForm codeCategoryForm() {
    return new CodeCategoryForm();
  }

  @ModelAttribute("searchCodeCategoryForm")
  public SearchCodeCategoryForm searchcodeCategoryForm() {
    return new SearchCodeCategoryForm();
  }

  @InitBinder("codeCategoryForm")
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(codeCategoryFormValidator);
  }

  @Override
  public String getFunctionName() {
    return "A_CODE_CATEGORY";
  }

  /**
   * 登録画面 初期表示
   *
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @GetMapping("/new")
  public String newCode(@ModelAttribute("codeCategoryForm") CodeCategoryForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("codeCategoryForm", new CodeCategoryForm());
    }

    return "modules/codeCategory/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PostMapping("/new")
  public String newCode(
      @Validated @ModelAttribute("codeCategoryForm") CodeCategoryForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/codeCategories/new";
    }

    // 入力値からDTOを作成する
    val inputCodeCategory = modelMapper.map(form, CodeCategory.class);

    // 登録する
    val createdCodeCategory = codeCategoryService.create(inputCodeCategory);

    // 登録成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_CREATED));

    return "redirect:/codeCategories/show/" + createdCodeCategory.getId();
  }

  /**
   * 一覧画面 初期表示
   *
   * @param form
   * @param init
   * @param pageable
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/find")
  public String findCodeCategory(
      @ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form,
      @RequestParam(name = "init", required = false) Boolean init,
      Pageable pageable,
      Model model) {
    // 検索条件の初期化
    if (isTrue(init)) {
      form = new SearchCodeCategoryForm();
      model.addAttribute("searchCodeCategoryForm", form);
    }

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, CodeCategoryCriteria.class);

    // 10件区切りで取得する
    val pages = codeCategoryService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    // 分類一覧
    val codeCategories = codeCategoryService.fetchAll();
    model.addAttribute("codeCategories", codeCategories);

    return "modules/codeCategory/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @PostMapping("/find")
  public String findCodeCategory(
      @Validated @ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/codeCategories/find";
    }

    return "redirect:/codeCategories/find";
  }

  /**
   * 詳細画面
   *
   * @param codeCategoryId
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/show/{codeCategoryId}")
  public String showCodeCategory(@PathVariable Long codeCategoryId, Model model) {
    val codeCategory = codeCategoryService.findById(codeCategoryId);
    model.addAttribute("codeCategory", codeCategory);
    return "modules/codeCategory/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param codeCategoryId
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/edit/{codeCategoryId}")
  public String editCodeCategory(
      @PathVariable Long codeCategoryId,
      @ModelAttribute("codeCategoryForm") CodeCategoryForm form,
      Model model) {
    // セッションから取得できる場合は、読み込み直さない
    if (!hasErrors(model)) {
      // 1件取得する
      val codeCategory = codeCategoryService.findById(codeCategoryId);

      // 取得したDtoをFromに詰め替える
      modelMapper.map(codeCategory, form);
    }

    return "modules/codeCategory/new";
  }

  /**
   * 編集画面 更新処理
   *
   * @param form
   * @param br
   * @param codeCategoryId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PostMapping("/edit/{codeCategoryId}")
  public String editCodeCategory(
      @Validated @ModelAttribute("codeCategoryForm") CodeCategoryForm form,
      BindingResult br,
      @PathVariable Long codeCategoryId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/codeCategories/edit/" + codeCategoryId;
    }

    // 更新対象を取得する
    val codeCategory = codeCategoryService.findById(codeCategoryId);

    // 入力値を詰め替える
    modelMapper.map(form, codeCategory);

    // 更新する
    val updatedCode = codeCategoryService.update(codeCategory);

    // セッションのcodeCategoryFormをクリアする
    sessionStatus.setComplete();

    // 更新成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_UPDATED));

    return "redirect:/codeCategories/show/" + updatedCode.getId();
  }

  /**
   * 削除処理
   *
   * @param codeCategoryId
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:save')")
  @PostMapping("/remove/{codeCategoryId}")
  public String removeCodeCategory(
      @PathVariable Long codeCategoryId, RedirectAttributes attributes) {
    // 論理削除する
    codeCategoryService.delete(codeCategoryId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/codeCategories/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @param form
   * @param response
   * @return
   */
  @PreAuthorize("hasAuthority('codeCategory:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, CodeCategoryCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      codeCategoryService.writeToOutputStream(outputStream, criteria, CodeCategoryCsv.class);
    }
  }
}
