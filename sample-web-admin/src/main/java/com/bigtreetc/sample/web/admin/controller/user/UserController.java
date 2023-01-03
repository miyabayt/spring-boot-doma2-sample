package com.bigtreetc.sample.web.admin.controller.user;

import static com.bigtreetc.sample.common.util.ValidateUtils.isTrue;
import static com.bigtreetc.sample.domain.util.TypeUtils.toListType;
import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.domain.entity.UploadFile;
import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.UserCriteria;
import com.bigtreetc.sample.domain.service.user.UserService;
import com.bigtreetc.sample.web.base.controller.html.AbstractHtmlController;
import com.bigtreetc.sample.web.base.util.MultipartFileUtils;
import com.bigtreetc.sample.web.base.view.CsvView;
import com.bigtreetc.sample.web.base.view.ExcelView;
import com.bigtreetc.sample.web.base.view.PdfView;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** ユーザー管理 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
@SessionAttributes(types = {SearchUserForm.class, UserForm.class})
@Slf4j
public class UserController extends AbstractHtmlController {

  @NonNull final UserFormValidator userFormValidator;

  @NonNull final UserService userService;

  @NonNull final PasswordEncoder passwordEncoder;

  @ModelAttribute("userForm")
  public UserForm userForm() {
    return new UserForm();
  }

  @ModelAttribute("searchUserForm")
  public SearchUserForm searchUserForm() {
    return new SearchUserForm();
  }

  @InitBinder("userForm")
  public void validatorBinder(WebDataBinder binder) {
    binder.addValidators(userFormValidator);
  }

  @Override
  public String getFunctionName() {
    return "A_USER";
  }

  /**
   * 登録画面 初期表示
   *
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('user:save')")
  @GetMapping("/new")
  public String newUser(@ModelAttribute("userForm") UserForm form, Model model) {
    if (!form.isNew()) {
      // SessionAttributeに残っている場合は再生成する
      model.addAttribute("userForm", new UserForm());
    }

    return "modules/user/new";
  }

  /**
   * 登録処理
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('user:save')")
  @PostMapping("/new")
  public String newUser(
      @Validated @ModelAttribute("userForm") UserForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/users/new";
    }

    // 入力値からDTOを作成する
    val inputUser = modelMapper.map(form, User.class);
    val password = form.getPassword();

    // パスワードをハッシュ化する
    inputUser.setPassword(passwordEncoder.encode(password));

    // 登録する
    val createdUser = userService.create(inputUser);

    // 登録成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_CREATED));

    return "redirect:/users/show/" + createdUser.getId();
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
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping("/find")
  public String findUser(
      @ModelAttribute("searchUserForm") SearchUserForm form,
      @RequestParam(name = "init", required = false) Boolean init,
      Pageable pageable,
      Model model) {
    // 検索条件の初期化
    if (isTrue(init)) {
      form = new SearchUserForm();
      model.addAttribute("searchUserForm", form);
    }

    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, UserCriteria.class);

    // 10件区切りで取得する
    val pages = userService.findAll(criteria, pageable);

    // 画面に検索結果を渡す
    model.addAttribute("pages", pages);

    return "modules/user/find";
  }

  /**
   * 検索結果
   *
   * @param form
   * @param br
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @PostMapping("/find")
  public String findUser(
      @Validated @ModelAttribute("searchUserForm") SearchUserForm form,
      BindingResult br,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/users/find";
    }

    return "redirect:/users/find";
  }

  /**
   * 詳細画面
   *
   * @param userId
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping("/show/{userId}")
  public String showUser(@PathVariable Long userId, Model model) {
    val user = userService.findById(userId);
    model.addAttribute("user", user);

    if (user.getUploadFile() != null) {
      // 画像ファイルを取得する
      val uploadFile = user.getUploadFile();

      // 画像ファイルをBase64化する
      val base64data = uploadFile.getContent().toBase64();
      model.addAttribute("image", "data:image/png;base64," + base64data);
    }

    return "modules/user/show";
  }

  /**
   * 編集画面 初期表示
   *
   * @param userId
   * @param form
   * @param model
   * @return
   */
  @PreAuthorize("hasAuthority('user:save')")
  @GetMapping("/edit/{userId}")
  public String editUser(
      @PathVariable Long userId, @ModelAttribute("userForm") UserForm form, Model model) {

    // セッションから取得できる場合は、読み込み直さない
    if (!hasErrors(model)) {
      // 1件取得する
      val user = userService.findById(userId);

      // 取得したDtoをFromに詰め替える
      modelMapper.map(user, form);
    }

    return "modules/user/new";
  }

  /**
   * 編集画面 更新処理
   *
   * @param form
   * @param br
   * @param userId
   * @param sessionStatus
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('user:save')")
  @PostMapping("/edit/{userId}")
  public String editUser(
      @Validated @ModelAttribute("userForm") UserForm form,
      BindingResult br,
      @PathVariable Long userId,
      SessionStatus sessionStatus,
      RedirectAttributes attributes) {
    // 入力チェックエラーがある場合は、元の画面にもどる
    if (br.hasErrors()) {
      setFlashAttributeErrors(attributes, br);
      return "redirect:/users/edit/" + userId;
    }

    // 更新対象を取得する
    val user = userService.findById(userId);

    // 入力値を詰め替える
    modelMapper.map(form, user);

    // パスワードをハッシュ化する
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    val image = form.getUserImage();
    if (image != null && !image.isEmpty()) {
      val uploadFile = new UploadFile();
      MultipartFileUtils.convert(image, uploadFile);
      user.setUploadFile(uploadFile);
    }

    // 更新する
    val updatedUser = userService.update(user);

    // セッションのuserFormをクリアする
    sessionStatus.setComplete();

    // 更新成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_UPDATED));

    return "redirect:/users/show/" + updatedUser.getId();
  }

  /**
   * 削除処理
   *
   * @param userId
   * @param attributes
   * @return
   */
  @PreAuthorize("hasAuthority('user:save')")
  @PostMapping("/remove/{userId}")
  public String removeUser(@PathVariable Long userId, RedirectAttributes attributes) {
    // 論理削除する
    userService.delete(userId);

    // 削除成功メッセージ
    attributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage(MESSAGE_DELETED));

    return "redirect:/users/find";
  }

  /**
   * CSVダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping("/download/{filename:.+\\.csv}")
  public ModelAndView downloadCsv(
      @PathVariable String filename, @ModelAttribute("searchUserForm") SearchUserForm form) {
    // 入力値から検索条件を作成する
    val criteria = modelMapper.map(form, UserCriteria.class);

    // 全件取得する
    val users = userService.findAll(criteria, Pageable.unpaged());

    // 詰め替える
    List<UserCsv> csvList = modelMapper.map(users.getContent(), toListType(UserCsv.class));

    // CSVスキーマクラス、データ、ダウンロード時のファイル名を指定する
    val view = new CsvView(UserCsv.class, csvList, filename);

    return new ModelAndView(view);
  }

  /**
   * Excelダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping(path = "/download/{filename:.+\\.xlsx}")
  public ModelAndView downloadExcel(@PathVariable String filename) {
    // 全件取得する
    val users = userService.findAll(new UserCriteria(), Pageable.unpaged());

    // Excelプック生成コールバック、データ、ダウンロード時のファイル名を指定する
    val view = new ExcelView(new UserExcel(), users.getContent(), filename);

    return new ModelAndView(view);
  }

  /**
   * PDFダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping(path = "/download/{filename:.+\\.pdf}")
  public ModelAndView downloadPdf(@PathVariable String filename) {
    // 全件取得する
    val users = userService.findAll(new UserCriteria(), Pageable.unpaged());

    // 帳票レイアウト、データ、ダウンロード時のファイル名を指定する
    val view = new PdfView("reports/users.jrxml", users.getContent(), filename);

    return new ModelAndView(view);
  }
}
