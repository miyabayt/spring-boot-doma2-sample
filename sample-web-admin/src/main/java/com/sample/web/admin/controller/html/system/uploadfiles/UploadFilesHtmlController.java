package com.sample.web.admin.controller.html.system.uploadfiles;

import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.common.util.FileUtils;
import com.sample.domain.helper.FileHelper;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.view.FileDownloadView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/uploadfiles")
@Slf4j
public class UploadFilesHtmlController extends AbstractHtmlController implements InitializingBean {

    @Value("${application.fileUploadLocation:#{systemProperties['java.io.tmpdir']}}") // 設定ファイルに定義されたアップロード先を取得する
    String fileUploadLocation;

    @Autowired
    FileHelper fileHelper;

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
        val stream = fileHelper.listAllFiles(location);
        val filenames = stream.map(path -> path.getFileName().toString()).collect(toList());

        model.addAttribute("filenames", filenames);

        return "modules/system/uploadfiles/list";
    }

    /**
     * ファイル内容表示
     * 
     * @param filename
     * @return
     */
    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ModelAndView serveFile(@PathVariable String filename) {
        // ファイルを読み込む
        val resource = fileHelper.loadFile(Paths.get(fileUploadLocation), filename);

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
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ModelAndView downloadFile(@PathVariable String filename) {
        // ファイルを読み込む
        val resource = fileHelper.loadFile(Paths.get(fileUploadLocation), filename);

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
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        // ファイルを保存する
        fileHelper.saveFile(Paths.get(fileUploadLocation), file);

        // リダイレクト先で完了メッセージを表示する
        redirectAttributes.addFlashAttribute(GLOBAL_MESSAGE, getMessage("uploadfiles.upload.success"));

        return "redirect:/system/uploadfiles/list";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // アップロードディレクトリ
        val location = Paths.get(fileUploadLocation);

        // ディレクトリがない場合は作成する
        FileUtils.createDirectories(location);
    }
}
