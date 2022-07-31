package com.sample.web.admin.controller.html.system.roles;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import com.sample.domain.dto.system.*;
import com.sample.domain.service.system.PermissionService;
import com.sample.domain.service.system.RoleService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.CsvView;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
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

/** ロール管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/system/roles")
@SessionAttributes(types = {RoleForm.class})
@Slf4j
public class RoleHtmlController extends AbstractHtmlController {

  @NonNull final RoleFormValidator roleFormValidator;

  @NonNull final RoleService roleService;

  @NonNull final PermissionService permissionService;

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
    return "A_ROLE";
  }

  /**
   * 登録画面 初期表示
   *
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('role:save')")
  @GetMapping("/new")
  public String newRole(@ModelAttribute("roleForm") RoleForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("roleForm", new RoleForm());
    }

    val permissions = getPermissions();
    model.addAttribute("permissions", permissions);

    return "modules/system/roles/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('role:save')")
  @PostMapping("/new")
  public String newRole(
      @Validated @ModelAttribute("roleForm") RoleForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/roles/new";
    }

    // 入力値からDTOを作成する
    val inputRole = modelMapper.map(form, Role.class);
    for (val entry : form.getPermissions().entrySet()) {
      val rp = new RolePermission();
      rp.setRoleCode(form.getRoleCode());
      rp.setPermissionCode(entry.getKey());
      rp.setIsEnabled(entry.getValue());
      inputRole.getRolePermissions().add(rp);
    }

    // 登録する
    val createdRole = roleService.create(inputRole);

    return "redirect:/system/roles/show/" + createdRole.getId();
  }

  /**
   * 一覧画面 初期表示
   *
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('role:read')")
  @GetMapping("/find")
  public String findRole(@ModelAttribute SearchRoleForm form, Pageable pageable, Model model) {
    // 入力値を詰め替える
    val criteria = modelMapper.map(form, RoleCriteria.class);

    // 10件区切りで取得する
    val pages = roleService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    return "modules/system/roles/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('role:read')")
  @PostMapping("/find")
  public String findRole(
      @Validated @ModelAttribute("searchRoleForm") SearchRoleForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/roles/find";
    }

    return "redirect:/system/roles/find";
  }

  /**
   * 詳細画面
   *
   * @param roleId
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('role:read')")
  @GetMapping("/show/{roleId}")
  public String showRole(@PathVariable Long roleId, Model model) {
    val role = roleService.findById(roleId);
    model.addAttribute("role", role);

    val permissions = getPermissions();
    model.addAttribute("permissions", permissions);

    return "modules/system/roles/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param roleId
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('role:save')")
  @GetMapping("/edit/{roleId}")
  public String editRole(
      @PathVariable Long roleId, @ModelAttribute("roleForm") RoleForm form, Model model) {
    // セッションから取得できる場合は、読み込み直さない
    if (!hasErrors(model)) {
      // 1件取得する
      val role = roleService.findById(roleId);

      // 取得したDtoをFromに詰め替える
      modelMapper.map(role, form);
      for (val p : role.getPermissions()) {
        val permissionCode = p.getPermissionCode();
        val isEnabled = role.hasPermission(permissionCode);
        form.getPermissions().put(permissionCode, isEnabled);
      }
    }

    // 権限一覧を取得する
    val permissions = getPermissions();
    model.addAttribute("permissions", permissions);

    return "modules/system/roles/new";
  }

  /**
   * 編集画面 更新処理
   *
   * @param form
   * @param br
   * @param roleId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('role:save')")
  @PostMapping("/edit/{roleId}")
  public String editRole(
      @Validated @ModelAttribute("roleForm") RoleForm form,
      BindingResult br,
      @PathVariable Long roleId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/system/roles/edit/" + roleId;
    }

    // 更新対象を取得する
    val role = roleService.findById(roleId);
    val permissions = form.getPermissions();
    for (val entry : permissions.entrySet()) {
      val permissionCode = entry.getKey();
      val isEnabled = Boolean.TRUE.equals(entry.getValue());
      role.setPermission(permissionCode, isEnabled);
    }

    // 入力値を詰め替える
    modelMapper.map(form, role);

    // 更新する
    val updatedRole = roleService.update(role);

    // セッションのroleFormをクリアする
    sessionStatus.setComplete();

    return "redirect:/system/roles/show/" + updatedRole.getId();
  }

  /**
   * 削除処理
   *
   * @param roleId
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('role:save')")
  @PostMapping("/remove/{roleId}")
  public String removeRole(@PathVariable Long roleId, RedirectAttributes attributes) {
    // 論理削除する
    roleService.delete(roleId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/system/roles/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('role:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public ModelAndView downloadCsv(@PathVariable String filename) {
    // 全件取得する
    val roles = roleService.findAll(new RoleCriteria(), Pageable.unpaged());

    // 詰め替える
    List<RoleCsv> csvList = modelMapper.map(roles.getContent(), toListType(RoleCsv.class));

    // CSVスキーマクラス、データ、ダウンロード時のファイル名を指定する
    val view = new CsvView(RoleCsv.class, csvList, filename);

    return new ModelAndView(view);
  }

  private Page<Permission> getPermissions() {
    return permissionService.findAll(new PermissionCriteria(), Pageable.unpaged());
  }
}
