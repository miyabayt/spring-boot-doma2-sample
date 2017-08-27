package com.sample.web.admin.controller.html.staffs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.domain.dto.Staff;
import com.sample.domain.dto.common.DefaultPageable;
import com.sample.domain.dto.common.ID;
import com.sample.domain.service.staff.StaffService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 担当者管理
 */
@Controller
@RequestMapping("/staffs")
@SessionAttributes(types = { SearchStaffForm.class, StaffForm.class })
@Slf4j
public class StaffHtmlController extends AbstractHtmlController {

    @Autowired
    StaffFormValidator staffFormValidator;

    @Autowired
    StaffService staffService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        return "A_Staff";
    }

    /**
     * 登録画面 初期表示
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newStaff(@ModelAttribute("staffForm") StaffForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttributeに残っている場合は再生成する
            model.addAttribute("staffForm", new StaffForm());
        }

        // 登録処理の入力チェック結果を引き継ぐ
        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "staffForm";
            model.addAttribute(key, model.asMap().get("errors"));
        }

        return "staffs/new";
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
    public String newStaff(@Validated @ModelAttribute("staffForm") StaffForm form, BindingResult result,
            RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/staffs/new";
        }

        // 入力値からDTOを作成する
        val inputStaff = modelMapper.map(form, Staff.class);
        val password = form.getPassword();

        // パスワードをハッシュ化する
        inputStaff.setPassword(passwordEncoder.encode(password));

        // 登録する
        val createdStaff = staffService.create(inputStaff);

        return "redirect:/staffs/show/" + createdStaff.getId().getValue();
    }

    /**
     * 一覧画面 初期表示
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findStaff(@ModelAttribute SearchStaffForm form, Model model) {
        // 入力値を詰め替える
        val where = modelMapper.map(form, Staff.class);
        where.setLastName(form.getName());
        where.setFirstName(form.getName());

        // 10件区切りで取得する
        val pages = staffService.findAll(where, form);

        // 画面に検索結果を渡す
        model.addAttribute("pages", pages);

        return "staffs/find";
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
    public String findStaff(@Validated @ModelAttribute("searchStaffForm") SearchStaffForm form, BindingResult result,
            RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
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
    @GetMapping("/show/{staffId}")
    public String showStaff(@PathVariable Integer staffId, Model model) {
        // 1件取得する
        val staff = staffService.findById(ID.of(staffId));
        model.addAttribute("staff", staff);
        return "staffs/show";
    }

    /**
     * 編集画面 初期表示
     *
     * @param staffId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{staffId}")
    public String editStaff(@PathVariable Integer staffId, @ModelAttribute("staffForm") StaffForm form, Model model) {
        // セッションから取得できる場合は、読み込み直さない
        if (form.getId() == null) {
            // 1件取得する
            val staff = staffService.findById(ID.of(staffId));

            // 取得したDtoをFromに詰め替える
            modelMapper.map(staff, form);
        }

        // 更新処理の入力チェック結果を引き継ぐ
        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "staffForm";
            model.addAttribute(key, model.asMap().get("errors"));
        }

        return "staffs/new";
    }

    /**
     * 編集画面 更新処理
     *
     * @param form
     * @param result
     * @param staffId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{staffId}")
    public String editStaff(@Validated @ModelAttribute("staffForm") StaffForm form, BindingResult result,
            @PathVariable Integer staffId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/staffs/edit/" + staffId;
        }

        // 更新対象を取得する
        val staff = staffService.findById(ID.of(staffId));

        // 入力値を詰め替える
        modelMapper.map(form, staff);
        val password = staff.getPassword();

        if (StringUtils.isNotEmpty(password)) {
            // パスワードをハッシュ化する
            staff.setPassword(passwordEncoder.encode(password));
        }

        // 更新する
        val updatedStaff = staffService.update(staff);

        // セッションのstaffFormをクリアする
        sessionStatus.setComplete();

        return "redirect:/staffs/show/" + updatedStaff.getId().getValue();
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
        val staffs = staffService.findAll(new Staff(), new DefaultPageable(1, Integer.MAX_VALUE));

        val listType = new TypeToken<List<StaffCsv>>() {
        }.getType();
        List<StaffCsv> csvList = modelMapper.map(staffs.getData(), listType);

        // レスポンスを設定する
        val view = new CsvView(StaffCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
