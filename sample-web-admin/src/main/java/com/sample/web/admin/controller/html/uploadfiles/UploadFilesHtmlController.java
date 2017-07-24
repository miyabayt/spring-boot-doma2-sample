package com.sample.web.admin.controller.html.uploadfiles;

import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.service.FileDownloadService;
import com.sample.web.base.service.FileUploadService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/uploadfiles")
@Slf4j
public class UploadFilesHtmlController extends AbstractHtmlController {

    @Value("${application.fileUploadLocation:#{systemProperties['java.io.tmpdir']}}") // 設定ファイルに定義されたアップロード先を取得する
    String fileUploadLocation;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    FileDownloadService fileDownloadService;

    @Override
    public String getFunctionName() {
        return "A_FileUpload";
    }

    /**
     * 一覧画面
     *
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/list")
    public String listFiles(Model model) throws IOException {
        // ファイル名のリストを作成する
        val location = Paths.get(fileUploadLocation);
        val stream = fileUploadService.listAllFiles(location);
        val filenames = stream.map(path -> path.getFileName().toString()).collect(Collectors.toList());

        model.addAttribute("filenames", filenames);

        return "uploadfiles/list";
    }

    /**
     * ファイル内容表示
     * 
     * @param filename
     * @return
     */
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        // ファイルを読み込む
        val resource = fileUploadService.loadFile(Paths.get(fileUploadLocation), filename);

        // レスポンスを作成する
        val response = fileDownloadService.createResponseEntity(resource, false);

        return response;
    }

    /**
     * ファイルダウンロード
     *
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        // ファイルを読み込む
        val resource = fileUploadService.loadFile(Paths.get(fileUploadLocation), filename);

        // レスポンスを作成する
        val response = fileDownloadService.createResponseEntity(resource, true);

        return response;
    }

    /**
     * ファイルアップロード
     * 
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        // ファイルを保存する
        fileUploadService.saveFile(Paths.get(fileUploadLocation), file);

        // リダイレクト先で完了メッセージを表示する
        redirectAttributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("uploadfiles.upload.success"));

        return "redirect:/uploadfiles/list";
    }
}
