package com.sample.web.admin.controller.html.roles;

import java.util.List;

import org.modelmapper.TypeToken;
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

import com.sample.domain.dto.Permission;
import com.sample.domain.dto.Role;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.service.permission.PermissionService;
import com.sample.domain.service.role.RoleService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 役割管理
 */
@Controller
@RequestMapping("/roles")
@SessionAttributes(types = { RoleForm.class })
@Slf4j
public class RoleHtmlController extends AbstractHtmlController {

    @Autowired
    RoleFormValidator roleFormValidator;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @ModelAttribute("roleForm")
    public RoleForm roleForm() {
        return new RoleForm();
    }

    @ModelAttribute("searchRoleForm")
    public SearchRoleForm searchRoleForm() {
        return new SearchRoleForm();
    }

    @InitBinder("roleForm")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(roleFormValidator);
    }

    @Override
    public String getFunctionName() {
        return "A_Role";
    }

    /**
     * 登録画面 初期表示
     *
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String newRole(@ModelAttribute("roleForm") RoleForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttributeに残っている場合は再生成する
            model.addAttribute("roleForm", new RoleForm());
        }

        // 登録処理の入力チェック結果を引き継ぐ
        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "roleForm";
            model.addAttribute(key, model.asMap().get("errors"));
        }

        // 権限一覧を取得する
        Page<Permission> permissions = permissionService.findAll(new Permission(), Pageable.NO_LIMIT_PAGEABLE);
        model.addAttribute("permissions", permissions);

        return "roles/new";
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
    public String newRole(@Validated @ModelAttribute("roleForm") RoleForm form, BindingResult result,
            RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/roles/new";
        }

        // 入力値からDTOを作成する
        val inputRole = modelMapper.map(form, Role.class);

        // 登録する
        val createdRole = roleService.create(inputRole);

        return "redirect:/roles/show/" + createdRole.getId().getValue();
    }

    /**
     * 一覧画面 初期表示
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findRole(@ModelAttribute SearchRoleForm form, Model model) {
        // 入力値を詰め替える
        val where = modelMapper.map(form, Role.class);

        // 10件区切りで取得する
        val pages = roleService.findAll(where, Pageable.DEFAULT_PAGEABLE);

        // 画面に検索結果を渡す
        model.addAttribute("pages", pages);

        return "roles/find";
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
    public String findRole(@Validated @ModelAttribute("searchRoleForm") SearchRoleForm form, BindingResult result,
            RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/roles/find";
        }

        return "redirect:/roles/find";
    }

    /**
     * 詳細画面
     *
     * @param roleId
     * @param model
     * @return
     */
    @GetMapping("/show/{roleId}")
    public String showRole(@PathVariable Integer roleId, Model model) {
        // 1件取得する
        val role = roleService.findById(ID.of(roleId));
        model.addAttribute("role", role);

        // 権限一覧を取得する
        Page<Permission> permissions = permissionService.findAll(new Permission(), Pageable.NO_LIMIT_PAGEABLE);
        model.addAttribute("permissions", permissions);

        return "roles/show";
    }

    /**
     * 編集画面 初期表示
     *
     * @param roleId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{roleId}")
    public String editRole(@PathVariable Integer roleId, @ModelAttribute("roleForm") RoleForm form, Model model) {
        // セッションから取得できる場合は、読み込み直さない
        if (form.getId() == null) {
            // 1件取得する
            val role = roleService.findById(ID.of(roleId));

            // 取得したDtoをFromに詰め替える
            modelMapper.map(role, form);
        }

        // 権限一覧を取得する
        Page<Permission> permissions = permissionService.findAll(new Permission(), Pageable.NO_LIMIT_PAGEABLE);
        model.addAttribute("permissions", permissions);

        // 更新処理の入力チェック結果を引き継ぐ
        if (model.containsAttribute("errors")) {
            val key = BindingResult.MODEL_KEY_PREFIX + "roleForm";
            model.addAttribute(key, model.asMap().get("errors"));
        }

        return "roles/new";
    }

    /**
     * 編集画面 更新処理
     *
     * @param form
     * @param result
     * @param roleId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{roleId}")
    public String editRole(@Validated @ModelAttribute("roleForm") RoleForm form, BindingResult result,
            @PathVariable Integer roleId, SessionStatus sessionStatus, RedirectAttributes attributes) {
        // 入力チェックエラーがある場合は、元の画面にもどる
        if (result.hasErrors()) {
            attributes.addFlashAttribute("errors", result);
            return "redirect:/roles/edit/" + roleId;
        }

        // 更新対象を取得する
        val role = roleService.findById(ID.of(roleId));

        // 入力値を詰め替える
        modelMapper.map(form, role);

        // 更新する
        val updatedRole = roleService.update(role);

        // セッションのroleFormをクリアする
        sessionStatus.setComplete();

        return "redirect:/roles/show/" + updatedRole.getId().getValue();
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
        val roles = roleService.findAll(new Role(), Pageable.NO_LIMIT_PAGEABLE);

        val listType = new TypeToken<List<RoleCsv>>() {
        }.getType();
        List<RoleCsv> csvList = modelMapper.map(roles.getData(), listType);

        // レスポンスを設定する
        val view = new CsvView(RoleCsv.class, csvList, filename);

        return new ModelAndView(view);
    }
}
