package com.sample.web.admin.controller.html.users.users;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import java.util.List;

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
import com.sample.domain.dto.system.UploadFile;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.service.users.UserService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.util.MultipartFileUtils;
import com.sample.web.base.view.CsvView;
import com.sample.web.base.view.ExcelView;
import com.sample.web.base.view.PdfView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ユーザー管理
 */
@Controller
@RequestMapping("/users/users")
@SessionAttributes(types = { SearchUserForm.class, UserForm.class })
@Slf4j
public class UserHtmlController extends AbstractHtmlController {

    @Autowired
    UserFormValidator userFormValidator;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    @GetMapping("/new")
    public String newUser(@ModelAttribute("userForm") UserForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttributeに残っている場合は再生成する
            model.addAttribute("userForm", new UserForm());
        }

        return "modules/users/users/new";
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
    public String newUser(@Validated @ModelAttribute("userForm") UserForm form, BindingResult br,
            RedirectAttributes attributes) {

        // 入力チェックエラーがある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/new";
        }

        // 入力値からDTOを作成する
        val inputUser = modelMapper.map(form, User.class);
        val password = form.getPassword();

        // パスワードをハッシュ化する
        inputUser.setPassword(passwordEncoder.encode(password));

        // 登録する
        val createdUser = userService.create(inputUser);

        return "redirect:/users/users/show/" + createdUser.getId();
    }

    /**
     * 一覧画面 初期表示
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findUser(@ModelAttribute SearchUserForm form, Model model) {
        // 入力値を詰め替える
        val criteria = modelMapper.map(form, UserCriteria.class);

        // 10件区切りで取得する
        val pages = userService.findAll(criteria, form);

        // 画面に検索結果を渡す
        model.addAttribute("pages", pages);

        return "modules/users/users/find";
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
    public String findUser(@Validated @ModelAttribute("searchUserForm") SearchUserForm form, BindingResult br,
            RedirectAttributes attributes) {

        // 入力チェックエラーがある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/find";
        }

        return "redirect:/users/users/find";
    }

    /**
     * 詳細画面
     *
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/show/{userId}")
    public String showUser(@PathVariable Long userId, Model model) {
        // 1件取得する
        val user = userService.findById(userId);
        model.addAttribute("user", user);

        if (user.getUploadFile() != null) {
            // 添付ファイルを取得する
            val uploadFile = user.getUploadFile();

            // Base64デコードして解凍する
            val base64data = uploadFile.getContent().toBase64();
            val sb = new StringBuilder().append("data:image/png;base64,").append(base64data);

            model.addAttribute("image", sb.toString());
        }

        return "modules/users/users/show";
    }

    /**
     * 編集画面 初期表示
     *
     * @param userId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable Long userId, @ModelAttribute("userForm") UserForm form, Model model) {

        // セッションから取得できる場合は、読み込み直さない
        if (!hasErrors(model)) {
            // 1件取得する
            val user = userService.findById(userId);

            // 取得したDtoをFromに詰め替える
            modelMapper.map(user, form);
        }

        return "modules/users/users/new";
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
    @PostMapping("/edit/{userId}")
    public String editUser(@Validated @ModelAttribute("userForm") UserForm form, BindingResult br,
            @PathVariable Long userId, SessionStatus sessionStatus, RedirectAttributes attributes) {

        // 入力チェックエラーがある場合は、元の画面にもどる
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/users/users/edit/" + userId;
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

        return "redirect:/users/users/show/" + updatedUser.getId();
    }

    /**
     * 削除処理
     *
     * @param userId
     * @param attributes
     * @return
     */
    @PostMapping("/remove/{userId}")
    public String removeUser(@PathVariable Long userId, RedirectAttributes attributes) {
        // 論理削除する
        userService.delete(userId);

        // 削除成功メッセージ
        attributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/users/users/find";
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
        val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

        // 詰め替える
        List<UserCsv> csvList = modelMapper.map(users.getData(), toListType(UserCsv.class));

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
    @GetMapping(path = "/download/{filename:.+\\.xlsx}")
    public ModelAndView downloadExcel(@PathVariable String filename) {
        // 全件取得する
        val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

        // Excelプック生成コールバック、データ、ダウンロード時のファイル名を指定する
        val view = new ExcelView(new UserExcel(), users.getData(), filename);

        return new ModelAndView(view);
    }

    /**
     * PDFダウンロード
     *
     * @param filename
     * @return
     */
    @GetMapping(path = "/download/{filename:.+\\.pdf}")
    public ModelAndView downloadPdf(@PathVariable String filename) {
        // 全件取得する
        val users = userService.findAll(new UserCriteria(), Pageable.NO_LIMIT);

        // 帳票レイアウト、データ、ダウンロード時のファイル名を指定する
        val view = new PdfView("reports/users.jrxml", users.getData(), filename);

        return new ModelAndView(view);
    }
}
