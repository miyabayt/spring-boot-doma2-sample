package com.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.val;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.AbstractView;

/** FileDownloadビュー */
public class FileDownloadView extends AbstractView {

  private int chunkSize = 256;

  private Resource resource;

  @Setter private boolean isAttachment = true;

  @Setter protected String filename;

  protected static final Tika TIKA = new Tika();

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
      val file = resource.getFile();
      val detectedContentType = TIKA.detect(file);
      val mediaType = MediaType.parseMediaType(detectedContentType);
      val inlineOrAttachment = (isAttachment) ? "attachment" : "inline";
      val contentDisposition = String.format("%s; filename=\"%s\"", inlineOrAttachment, filename);

      response.setHeader(CONTENT_TYPE, mediaType.toString());
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
