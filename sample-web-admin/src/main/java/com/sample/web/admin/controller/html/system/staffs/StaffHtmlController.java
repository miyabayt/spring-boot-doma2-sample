package com.sample.web.admin.controller.html.system.staffs;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.service.system.StaffService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 担当者管理
 */
@Controller
@RequestMapping("/system/staffs")
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
        return "A_STAFF";
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

        return "modules/system/staffs/new";
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
            setFlashAttributeErrors(attributes, result);
            return "redirect:/system/staffs/new";
        }

        // 入力値からDTOを作成する
        val inputStaff = modelMapper.map(form, Staff.class);
        val password = form.getPassword();

        // パスワードをハッシュ化する
        inputStaff.setPassword(passwordEncoder.encode(password));

        // 登録する
        val createdStaff = staffService.create(inputStaff);

        return "redirect:/system/staffs/show/" + createdStaff.getId();
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

        return "modules/system/staffs/find";
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
            setFlashAttributeErrors(attributes, result);
            return "redirect:/system/staffs/find";
        }

        return "redirect:/system/staffs/find";
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
        val staff = staffService.findById(staffId);
        model.addAttribute("staff", staff);
        return "modules/system/staffs/show";
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
        if (!hasErrors(model)) {
            // 1件取得する
            val staff = staffService.findById(staffId);

            // 取得したDtoをFromに詰め替える
            modelMapper.map(staff, form);
        }

        return "modules/system/staffs/new";
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
            setFlashAttributeErrors(attributes, result);
            return "redirect:/system/staffs/edit/" + staffId;
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

        // セッションのstaffFormをクリアする
        sessionStatus.setComplete();

        return "redirect:/system/staffs/show/" + updatedStaff.getId();
    }

    /**
     * 削除処理
     *
     * @param staffId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{staffId}")
    public String removeStaff(@PathVariable Integer staffId, RedirectAttributes attributes) {
        // 削除対象を取得する
        val staff = staffService.findById(staffId);

        // 論理削除する
        staffService.delete(staff.getId());

        // 削除成功メッセージ
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/system/staffs/find";
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
        val staffs = staffService.findAll(new Staff(), Pageable.NO_LIMIT_PAGEABLE);

        // 詰め替える
        List<StaffCsv> csvList = modelMapper.map(staffs.getData(), toListType(StaffCsv.class));

        // レスポンスを設定する
        val view = new CsvView(StaffCsv.class, csvList);
        view.setFilename(filename);

        return new ModelAndView(view);
    }
}
