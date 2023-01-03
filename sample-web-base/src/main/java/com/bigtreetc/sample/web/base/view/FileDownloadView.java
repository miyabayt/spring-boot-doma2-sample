package com.bigtreetc.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.val;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractView;

/** FileDownloadビュー */
public class FileDownloadView extends AbstractView {

  private int chunkSize;

  private Resource resource;

  @Setter private boolean isAttachment = true;

  @Setter protected String filename;

  /** コンストラクタ */
  public FileDownloadView(Resource resource) {
    this(resource, 256);
  }

  /** コンストラクタ */
  public FileDownloadView(Resource resource, int chunkSize) {
    this.resource = resource;
    this.chunkSize = chunkSize;
  }

  @Override
  protected final void renderMergedOutputModel(
      Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    try (InputStream inputStream = resource.getInputStream();
        OutputStream outputStream = response.getOutputStream()) {
      val inlineOrAttachment = (isAttachment) ? "attachment" : "inline";
      val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
      val contentDisposition =
          String.format("%s; filename*=UTF-8''%s", inlineOrAttachment, encodedFilename);

      response.setHeader(CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
      response.setHeader(CONTENT_DISPOSITION, contentDisposition);

      byte[] buffer = new byte[chunkSize];
      int length;
      while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.flush();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
