package com.bigtreetc.sample.web.admin.controller.holiday;

import static com.bigtreetc.sample.common.util.ValidateUtils.isTrue;
import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.domain.entity.Holiday;
import com.bigtreetc.sample.domain.entity.HolidayCriteria;
import com.bigtreetc.sample.domain.service.holiday.HolidayService;
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

/** 祝日管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/holidays")
@SessionAttributes(types = {SearchHolidayForm.class, HolidayForm.class})
@Slf4j
public class HolidayController extends AbstractHtmlController {

  @NonNull final HolidayFormValidator holidayFormValidator;

  @NonNull final HolidayService holidayService;

  @ModelAttribute("holidayForm")
  public HolidayForm holidayForm() {
    return new HolidayForm();
  }

  @ModelAttribute("searchHolidayForm")
  public SearchHolidayForm searchHolidayForm() {
    return new SearchHolidayForm();
  }

  @InitBinder("holidayForm")
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(holidayFormValidator);
  }

  @Override
  public String getFunctionName() {
    return "A_HOLIDAY";
  }

  /**
   * 登録画面 初期表示
   *
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:save')")
  @GetMapping("/new")
  public String newHoliday(@ModelAttribute("holidayForm") HolidayForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("holidayForm", new HolidayForm());
    }

    return "modules/holiday/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:save')")
  @PostMapping("/new")
  public String newHoliday(
      @Validated @ModelAttribute("holidayForm") HolidayForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/holidays/new";
    }

    // 入力値からDTOを作成する
    val inputHoliday = modelMapper.map(form, Holiday.class);

    // 登録する
    val createdHoliday = holidayService.create(inputHoliday);

    // 登録成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_CREATED));

    return "redirect:/holidays/show/" + createdHoliday.getId();
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
  @PreAuthorize("hasAuthority('holiday:read')")
  @GetMapping("/find")
  public String findHoliday(
      @ModelAttribute("searchHolidayForm") SearchHolidayForm form,
      @RequestParam(name = "init", required = false) Boolean init,
      Pageable pageable,
      Model model) {
    // 検索条件の初期化
    if (isTrue(init)) {
      form = new SearchHolidayForm();
      model.addAttribute("searchHolidayForm", form);
    }

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, HolidayCriteria.class);

    // 10件区切りで取得する
    val pages = holidayService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    return "modules/holiday/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:read')")
  @PostMapping("/find")
  public String findHoliday(
      @Validated @ModelAttribute("searchHolidayForm") SearchHolidayForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/holidays/find";
    }

    return "redirect:/holidays/find";
  }

  /**
   * 詳細画面
   *
   * @param holidayId
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:read')")
  @GetMapping("/show/{holidayId}")
  public String showHoliday(@PathVariable Long holidayId, Model model) {
    val holiday = holidayService.findById(holidayId);
    model.addAttribute("holiday", holiday);
    return "modules/holiday/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param holidayId
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:save')")
  @GetMapping("/edit/{holidayId}")
  public String editHoliday(
      @PathVariable Long holidayId, @ModelAttribute("holidayForm") HolidayForm form, Model model) {
    // セッションから取得できる場合は、読み込み直さない
    if (!hasErrors(model)) {
      // 1件取得する
      val holiday = holidayService.findById(holidayId);

      // 取得したDtoをFromに詰め替える
      modelMapper.map(holiday, form);
    }

    return "modules/holiday/new";
  }

  /**
   * 編集画面 更新処理
   *
   * @param form
   * @param br
   * @param holidayId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:save')")
  @PostMapping("/edit/{holidayId}")
  public String editHoliday(
      @Validated @ModelAttribute("holidayForm") HolidayForm form,
      BindingResult br,
      @PathVariable Long holidayId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/holidays/edit/" + holidayId;
    }

    // 更新対象を取得する
    val holiday = holidayService.findById(holidayId);

    // 入力値を詰め替える
    modelMapper.map(form, holiday);

    // 更新する
    val updatedHoliday = holidayService.update(holiday);

    // セッションのholidayFormをクリアする
    sessionStatus.setComplete();

    // 更新成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_UPDATED));

    return "redirect:/holidays/show/" + updatedHoliday.getId();
  }

  /**
   * 削除処理
   *
   * @param holidayId
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:save')")
  @PostMapping("/remove/{holidayId}")
  public String removeHoliday(@PathVariable Long holidayId, RedirectAttributes attributes) {
    // 論理削除する
    holidayService.delete(holidayId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/holidays/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @param form
   * @param response
   * @return
   */
  @PreAuthorize("hasAuthority('holiday:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public void downloadCsv(
      @PathVariable String filename,
      @ModelAttribute("searchHolidayForm") SearchHolidayForm form,
      HttpServletResponse response)
      throws IOException {
    // ダウンロード時のファイル名をセットする
    setContentDispositionHeader(response, filename, true);

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, HolidayCriteria.class);

    // CSV出力する
    try (val outputStream = response.getOutputStream()) {
      holidayService.writeToOutputStream(outputStream, criteria, HolidayCsv.class);
    }
  }
}
