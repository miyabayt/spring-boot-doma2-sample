package com.sample.web.admin.controller.html.system.codecategories;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.helper.CodeHelper;
import com.sample.domain.service.system.CodeCategoryService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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

/** コード分類管理 */
@Controller
@RequestMapping("/system/code_categories")
@SessionAttributes(types = {SearchCodeCategoryForm.class, CodeCategoryForm.class})
@Slf4j
public class CodeCategoryHtmlController extends AbstractHtmlController {

  @Autowired CodeCategoryFormValidator codeCategoryFormValidator;

  @Autowired CodeCategoryService codeCategoryService;

  @Autowired CodeHelper codeHelper;

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
  @GetMapping("/new")
  public String newCode(@ModelAttribute("codeCategoryForm") CodeCategoryForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("codeCategoryForm", new CodeCategoryForm());
    }

    return "modules/system/code_categories/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PostMapping("/new")
  public String newCode(
      @Validated @ModelAttribute("codeCategoryForm") CodeCategoryForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/code_categories/new";
    }

    // 入力値からDTOを作成する
    val inputCodeCategory = modelMapper.map(form, CodeCategory.class);

    // 登録する
    val createdCodeCategory = codeCategoryService.create(inputCodeCategory);

    return "redirect:/system/code_categories/show/" + createdCodeCategory.getId();
  }

  /**
   * 一覧画面 初期表示
   *
   * @param model
   * @return
   */
  @GetMapping("/find")
  public String findCodeCategory(
      @ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form, Model model) {
    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, CodeCategoryCriteria.class);

    // 10件区切りで取得する
    val pages = codeCategoryService.findAll(criteria, form);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    // カテゴリ分類一覧
    val codeCategories = codeHelper.getCodeCategories();
    model.addAttribute("codeCategories", codeCategories);

    return "modules/system/code_categories/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PostMapping("/find")
  public String findCodeCategory(
      @Validated @ModelAttribute("searchCodeCategoryForm") SearchCodeCategoryForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/code_categories/find";
    }

    return "redirect:/system/code_categories/find";
  }

  /**
   * 詳細画面
   *
   * @param codeCategoryId
   * @param model
   * @return
   */
  @GetMapping("/show/{codeCategoryId}")
  public String showCodeCategory(@PathVariable Long codeCategoryId, Model model) {
    // 1件取得する
    val codeCategory = codeCategoryService.findById(codeCategoryId);
    model.addAttribute("codeCategory", codeCategory);
    return "modules/system/code_categories/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param codeCategoryId
   * @param form
   * @param model
   * @return
   */
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

    return "modules/system/code_categories/new";
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
      return "redirect:/system/code_categories/edit/" + codeCategoryId;
    }

    // 更新対象を取得する
    val codeCategory = codeCategoryService.findById(codeCategoryId);

    // 入力値を詰め替える
    modelMapper.map(form, codeCategory);

    // 更新する
    val updatedCode = codeCategoryService.update(codeCategory);

    // セッションのcodeCategoryFormをクリアする
    sessionStatus.setComplete();

    return "redirect:/system/code_categories/show/" + updatedCode.getId();
  }

  /**
   * 削除処理
   *
   * @param codeCategoryId
   * @param attributes
   * @return
   */
  @PostMapping("/remove/{codeCategoryId}")
  public String removeCodeCategory(
      @PathVariable Long codeCategoryId, RedirectAttributes attributes) {
    // 論理削除する
    codeCategoryService.delete(codeCategoryId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/system/code_categories/find";
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
    val codes = codeCategoryService.findAll(new CodeCategoryCriteria(), Pageable.NO_LIMIT);

    // 詰め替える
    List<CodeCategoryCsv> csvList =
        modelMapper.map(codes.getData(), toListType(CodeCategoryCsv.class));

    // CSVスキーマクラス、データ、ダウンロード時のファイル名を指定する
    val view = new CsvView(CodeCategoryCsv.class, csvList, filename);

    return new ModelAndView(view);
  }
}
