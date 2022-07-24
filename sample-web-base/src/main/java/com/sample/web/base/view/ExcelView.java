package com.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.val;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

/** Excelビュー */
public class ExcelView extends AbstractXlsxView {

  protected String filename;

  protected Collection<?> data;

  protected Callback callback;

  /** コンストラクタ */
  public ExcelView() {
    setContentType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=Windows-31J;");
  }

  /**
   * コンストラクタ
   *
   * @param callback
   * @param data
   * @param filename
   */
  public ExcelView(Callback callback, Collection<?> data, String filename) {
    this();
    this.callback = callback;
    this.data = data;
    this.filename = filename;
  }

  @Override
  protected void buildExcelDocument(
      Map<String, Object> model,
      Workbook workbook,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {

    // ファイル名に日本語を含めても文字化けしないようにUTF-8にエンコードする
    val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
    val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
    response.setHeader(CONTENT_DISPOSITION, contentDisposition);

    // Excelブックを構築する
    callback.buildExcelWorkbook(model, this.data, workbook);
  }

  public interface Callback {

    /**
     * Excelブックを構築します。
     *
     * @param model
     * @param data
     * @param workbook
     */
    void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook);
  }
}
