package com.bigtreetc.sample.web.admin.controller.uploadfile;

import static com.bigtreetc.sample.web.base.WebConst.SUCCESS_MESSAGE;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.common.util.FileUtils;
import com.bigtreetc.sample.web.base.controller.html.AbstractHtmlController;
import com.bigtreetc.sample.web.base.util.MultipartFileUtils;
import com.bigtreetc.sample.web.base.view.FileDownloadView;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/uploadFiles")
@Slf4j
public class UploadFilesController extends AbstractHtmlController {

  @Value(
      "${application.fileUploadLocation:#{systemProperties['java.io.tmpdir']}}") // 設定ファイルに定義されたアップロード先を取得する
  String fileUploadLocation;

  @Override
  public String getFunctionName() {
    return "A_FILEUPLOAD";
  }

  /**
   * 一覧画面
   *
   * @param model
   * @return
   * @throws IOException
   */
  @PreAuthorize("hasAuthority('uploadFile')")
  @GetMapping("/list")
  public String listFiles(Model model) throws IOException {
    // ファイル名のリストを作成する
    val location = Paths.get(fileUploadLocation);
    val paths = FileUtils.listAllFiles(location);
    val filenames = paths.stream().map(path -> path.getFileName().toString()).collect(toList());

    model.addAttribute("filenames", filenames);

    return "modules/uploadFile/list";
  }

  /**
   * ファイル内容表示
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('uploadFile')")
  @GetMapping("/{filename:.+}")
  @ResponseBody
  public ModelAndView serveFile(@PathVariable String filename) {
    // ファイルを読み込む
    val resource = FileUtils.loadFile(Paths.get(fileUploadLocation), filename);

    // レスポンスを設定する
    val view = new FileDownloadView(resource);
    view.setAttachment(false);
    view.setFilename(filename);

    return new ModelAndView(view);
  }

  /**
   * ファイルダウンロード
   *
   * @param filename
   * @return
   */
  @PreAuthorize("hasAuthority('uploadFile')")
  @GetMapping("/download/{filename:.+}")
  @ResponseBody
  public ModelAndView downloadFile(@PathVariable String filename) {
    // ファイルを読み込む
    val resource = FileUtils.loadFile(Paths.get(fileUploadLocation), filename);

    // レスポンスを設定する
    val view = new FileDownloadView(resource);
    view.setFilename(filename);

    return new ModelAndView(view);
  }

  /**
   * ファイルアップロード
   *
   * @param file
   * @param redirectAttributes
   * @return
   */
  @PreAuthorize("hasAuthority('uploadFile')")
  @PostMapping("/upload")
  public String uploadFile(
      @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    // ファイルを保存する
    MultipartFileUtils.saveFile(Paths.get(fileUploadLocation), file);

    // アップロード成功メッセージ
    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage("uploadFiles.uploaded"));

    return "redirect:/uploadFiles/list";
  }

  /**
   * ファイルアップロード（Ajax）
   *
   * @param file
   * @return
   */
  @PreAuthorize("hasAuthority('uploadFile')")
  @PostMapping(
      path = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      headers = "x-requested-with=XMLHttpRequest")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
    // ファイルを保存する
    MultipartFileUtils.saveFile(Paths.get(fileUploadLocation), file);
    val body =
        Map.<String, Object>of("message", getMessage("uploadFiles.uploaded"), "success", true);
    return ResponseEntity.ok().body(body);
  }

  /**
   * ファイル削除
   *
   * @param filename
   * @param redirectAttributes
   * @return
   */
  @PreAuthorize("hasAuthority('uploadFile')")
  @DeleteMapping(path = "/delete/{filename:.+}")
  public String deleteFile(@PathVariable String filename, RedirectAttributes redirectAttributes) {
    // ファイルを削除する
    FileUtils.deleteFile(Paths.get(fileUploadLocation), filename);

    // 削除成功メッセージ
    redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, getMessage("uploadFiles.deleted"));

    return "redirect:/uploadFiles/list";
  }

  @PostConstruct
  public void init() throws Exception {
    // アップロードディレクトリ
    val location = Paths.get(fileUploadLocation);

    // ディレクトリがない場合は作成する
    FileUtils.createDirectories(location);
  }
}
