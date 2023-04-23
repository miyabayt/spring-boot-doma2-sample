package com.bigtreetc.sample.web.admin.controller.staff;

import static com.bigtreetc.sample.common.util.ValidateUtils.isTrue;
import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.domain.entity.Staff;
import com.bigtreetc.sample.domain.entity.StaffCriteria;
import com.bigtreetc.sample.domain.service.staff.StaffService;
import com.bigtreetc.sample.web.base.controller.html.AbstractHtmlController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** 担当者管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/staffs")
@SessionAttributes(types = {SearchStaffForm.class, StaffForm.class})
@Slf4j
public class StaffController extends AbstractHtmlController {

  @NonNull final StaffFormValidator staffFormValidator;

  @NonNull final StaffService staffService;

  @NonNull final PasswordEncoder passwordEncoder;

  @ModelAttribute("staffForm")
  public StaffForm staffForm() {
    return new StaffForm();
  }

  @ModelAttribute("searchStaffForm")
  public SearchStaffForm searchStaffForm() {
    return new SearchStaffForm();
  }

  @InitBinder("staffForm")
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(staffFormValidator);
  }

  @Override
  public String getFunctionName() {
    return "A_STAFF";
  }

  /**
   * 登録画面 初期表示
   *
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('staff:save')")
  @GetMapping("/new")
  public String newStaff(@ModelAttribute("staffForm") StaffForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("staffForm", new StaffForm());
    }

    return "modules/staff/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('staff:save')")
  @PostMapping("/new")
  public String newStaff(
      @Validated @ModelAttribute("staffForm") StaffForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/staffs/new";
    }

    // 入力値からDTOを作成する
    val inputStaff = modelMapper.map(form, Staff.class);
    val password = form.getPassword();

    // パスワードをハッシュ化する
    inputStaff.setPassword(passwordEncoder.encode(password));

    // 登録する
    val createdStaff = staffService.create(inputStaff);

    // 登録成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_CREATED));

    return "redirect:/staffs/show/" + createdStaff.getId();
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
  @PreAuthorize("hasAuthority('staff:read')")
  @GetMapping("/find")
  public String findStaff(
      @ModelAttribute("searchStaffForm") SearchStaffForm form,
      @RequestParam(name = "init", required = false) Boolean init,
      Pageable pageable,
      Model model) {
    // 検索条件の初期化
    if (isTrue(init)) {
      form = new SearchStaffForm();
      model.addAttribute("searchStaffForm", form);
    }

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, StaffCriteria.class);

    // 10件区切りで取得する
    val pages = staffService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    return "modules/staff/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('staff:read')")
  @PostMapping("/find")
  public String findStaff(
      @Validated @ModelAttribute("searchStaffForm") SearchStaffForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/staffs/find";
    }

    return "redirect:/staffs/find";
  }

  /**
   * 詳細画面
   *
   * @param staffId
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('staff:read')")
  @GetMapping("/show/{staffId}")
  public String showStaff(@PathVariable Long staffId, Model model) {
    val staff = staffService.findById(staffId);
    model.addAttribute("staff", staff);
    return "modules/staff/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param staffId
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('staff:save')")
  @GetMapping("/edit/{staffId}")
  public String editStaff(
      @PathVariable Long staffId, @ModelAttribute("staffForm") StaffForm form, Model model) {
    // セッションから取得できる場合は、読み込み直さない
    if (!hasErrors(model)) {
      // 1件取得する
      val staff = staffService.findById(staffId);

      // 取得したDtoをFromに詰め替える
      modelMapper.map(staff, form);
    }

    return "modules/staff/new";
  }

  /**
   * 編集画面 更新処理
   *
   * @param form
   * @param br
   * @param staffId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('staff:save')")
  @PostMapping("/edit/{staffId}")
  public String editStaff(
      @Validated @ModelAttribute("staffForm") StaffForm form,
      BindingResult br,
      @PathVariable Long staffId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/staffs/edit/" + staffId;
    }

    // 更新対象を取得する
    val staff = staffService.findById(staffId);

    // 入力値を詰め替える
    modelMapper.map(form, staff);
    val password = staff.getPassword();

    if (StringUtils.isNotEmpty(password)) {
      // パスワードをハッシュ化する
      staff.setPassword(passwordEncoder.encode(password));
    }

    // 更新する
    val updatedStaff = staffService.update(staff);

    // セッションのFormをクリアする
    sessionStatus.setComplete();

    // 更新成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_UPDATED));

    return "redirect:/staffs/show/" + updatedStaff.getId();
  }

  /**
   * 削除処理
   *
   * @param staffId
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('staff:save')")
  @PostMapping("/remove/{staffId}")
  public String removeStaff(@PathVariable Long staffId, RedirectAttributes attributes) {
    // 論理削除する
    staffService.delete(staffId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/staffs/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @param form
   * @return
   */
  @PreAuthorize("hasAuthority('staff:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute("searchStaffForm") SearchStaffForm form,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, StaffCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      staffService.writeToOutputStream(outputStream, criteria, StaffCsv.class);
    }
  }
}
