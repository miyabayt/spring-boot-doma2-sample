package com.sample.web.admin.controller.html.system.mailtemplates;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;
import com.sample.domain.service.system.MailTemplateService;
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

/** メールテンプレート管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/system/mailTemplates")
@SessionAttributes(types = {SearchMailTemplateForm.class, MailTemplateForm.class})
@Slf4j
public class MailTemplateHtmlController extends AbstractHtmlController {

  @NonNull final MailTemplateFormValidator mailTemplateFormValidator;

  @NonNull final MailTemplateService mailTemplateService;

  @ModelAttribute("mailTemplateForm")
  public MailTemplateForm mailTemplateForm() {
    return new MailTemplateForm();
  }

  @ModelAttribute("searchMailTemplateForm")
  public SearchMailTemplateForm searchMailTemplateForm() {
    return new SearchMailTemplateForm();
  }

  @InitBinder("mailTemplateForm")
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(mailTemplateFormValidator);
  }

  @Override
  public String getFunctionName() {
    return "A_MAIL_TEMPLATE";
  }

  /**
   * 登録画面 初期表示
   *
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:save')")
  @GetMapping("/new")
  public String newMailTemplate(
      @ModelAttribute("mailTemplateForm") MailTemplateForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("mailTemplateForm", new MailTemplateForm());
    }

    return "modules/system/mailTemplates/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:save')")
  @PostMapping("/new")
  public String newMailTemplate(
      @Validated @ModelAttribute("mailTemplateForm") MailTemplateForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/mailTemplates/new";
    }

    // 入力値からDTOを作成する
    val inputMailTemplate = modelMapper.map(form, MailTemplate.class);

    // 登録する
    val createdMailTemplate = mailTemplateService.create(inputMailTemplate);

    return "redirect:/system/mailTemplates/show/" + createdMailTemplate.getId();
  }

  /**
   * 一覧画面 初期表示
   *
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @GetMapping("/find")
  public String findMailTemplate(
      @ModelAttribute SearchMailTemplateForm form, Pageable pageable, Model model) {
    // 入力値を詰め替える
    val criteria = modelMapper.map(form, MailTemplateCriteria.class);

    // 10件区切りで取得する
    val pages = mailTemplateService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    return "modules/system/mailTemplates/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @PostMapping("/find")
  public String findMailTemplate(
      @Validated @ModelAttribute("searchMailTemplateForm") SearchMailTemplateForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/mailTemplates/find";
    }

    return "redirect:/system/mailTemplates/find";
  }

  /**
   * 詳細画面
   *
   * @param mailTemplateId
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @GetMapping("/show/{mailTemplateId}")
  public String showMailTemplate(@PathVariable Long mailTemplateId, Model model) {
    val mailTemplate = mailTemplateService.findById(mailTemplateId);
    model.addAttribute("mailTemplate", mailTemplate);
    return "modules/system/mailTemplates/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param mailTemplateId
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:save')")
  @GetMapping("/edit/{mailTemplateId}")
  public String editMailTemplate(
      @PathVariable Long mailTemplateId,
      @ModelAttribute("mailTemplateForm") MailTemplateForm form,
      Model model) {
    // セッションから取得できる場合は、読み込み直さない
    if (!hasErrors(model)) {
      // 1件取得する
      val mailTemplate = mailTemplateService.findById(mailTemplateId);

      // 取得したDtoをFromに詰め替える
      modelMapper.map(mailTemplate, form);
    }

    return "modules/system/mailTemplates/new";
  }

  /**
   * 編集画面 更新処理
   *
   * @param form
   * @param br
   * @param mailTemplateId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:save')")
  @PostMapping("/edit/{mailTemplateId}")
  public String editMailTemplate(
      @Validated @ModelAttribute("mailTemplateForm") MailTemplateForm form,
      BindingResult br,
      @PathVariable Long mailTemplateId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/mailTemplates/edit/" + mailTemplateId;
    }

    // 更新対象を取得する
    val mailTemplate = mailTemplateService.findById(mailTemplateId);

    // 入力値を詰め替える
    modelMapper.map(form, mailTemplate);

    // 更新する
    val updatedMailTemplate = mailTemplateService.update(mailTemplate);

    // セッションのmailTemplateFormをクリアする
    sessionStatus.setComplete();

    return "redirect:/system/mailTemplates/show/" + updatedMailTemplate.getId();
  }

  /**
   * 削除処理
   *
   * @param mailTemplateId
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:save')")
  @PostMapping("/remove/{mailTemplateId}")
  public String removeMailTemplate(
      @PathVariable Long mailTemplateId, RedirectAttributes attributes) {
    // 論理削除する
    mailTemplateService.delete(mailTemplateId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/system/mailTemplates/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('mailTemplate:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public ModelAndView downloadCsv(@PathVariable String filename) {
    // 全件取得する
    val mailTemplates = mailTemplateService.findAll(new MailTemplateCriteria(), Pageable.unpaged());

    // 詰め替える
    List<MailTemplateCsv> csvList =
        modelMapper.map(mailTemplates.getContent(), toListType(MailTemplateCsv.class));

    // CSVスキーマクラス、データ、ダウンロード時のファイル名を指定する
    val view = new CsvView(MailTemplateCsv.class, csvList, filename);

    return new ModelAndView(view);
  }
}
