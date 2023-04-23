package com.bigtreetc.sample.web.admin.controller.code;

import static com.bigtreetc.sample.common.util.ValidateUtils.isTrue;
import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.domain.entity.Code;
import com.bigtreetc.sample.domain.entity.CodeCriteria;
import com.bigtreetc.sample.domain.service.code.CodeService;
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

/** コード管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/codes")
@SessionAttributes(types = {SearchCodeForm.class, CodeForm.class})
@Slf4j
public class CodeController extends AbstractHtmlController {

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

    // 分類一覧
    val codeCategories = codeCategoryService.fetchAll();
    model.addAttribute("codeCategories", codeCategories);

    return "modules/code/new";
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
      return "redirect:/codes/new";
    }

    // 入力値からDTOを作成する
    val inputCode = modelMapper.map(form, Code.class);

    // 登録する
    val createdCode = codeService.create(inputCode);

    // 登録成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_CREATED));

    return "redirect:/codes/show/" + createdCode.getId();
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
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/find")
  public String findCode(
      @ModelAttribute("searchCodeForm") SearchCodeForm form,
      @RequestParam(name = "init", required = false) Boolean init,
      Pageable pageable,
      Model model) {
    // 検索条件の初期化
    if (isTrue(init)) {
      form = new SearchCodeForm();
      model.addAttribute("searchCodeForm", form);
    }

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, CodeCriteria.class);

    // 10件区切りで取得する
    val pages = codeService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    // カテゴリ分類一覧
    val codeCategories = codeCategoryService.fetchAll();
    model.addAttribute("codeCategories", codeCategories);

    return "modules/code/find";
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
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/show/{codeId}")
  public String showCode(@PathVariable Long codeId, Model model) {
    val code = codeService.findById(codeId);
    model.addAttribute("code", code);
    return "modules/code/show";
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

    // 分類一覧
    val codeCategories = codeCategoryService.fetchAll();
    model.addAttribute("codeCategories", codeCategories);

    return "modules/code/new";
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
      return "redirect:/codes/edit/" + codeId;
    }

    // 更新対象を取得する
    val code = codeService.findById(codeId);

    // 入力値を詰め替える
    modelMapper.map(form, code);

    // 更新する
    val updatedCode = codeService.update(code);

    // セッションのcodeFormをクリアする
    sessionStatus.setComplete();

    // 更新成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_UPDATED));

    return "redirect:/codes/show/" + updatedCode.getId();
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
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/codes/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @param form
   * @param response
   * @return
   */
  @PreAuthorize("hasAuthority('code:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute("searchCodeForm") SearchCodeForm form,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, CodeCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      codeService.writeToOutputStream(outputStream, criteria, CodeCsv.class);
    }
  }
}
