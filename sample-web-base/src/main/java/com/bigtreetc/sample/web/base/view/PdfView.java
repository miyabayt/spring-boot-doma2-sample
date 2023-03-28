package com.bigtreetc.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import lombok.val;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.AbstractView;

/** PDFビュー */
public class PdfView extends AbstractView {

  protected String report;

  protected Collection<?> data;

  protected String filename;

  /**
   * コンストラクタ
   *
   * @param report
   * @param data
   * @param filename
   */
  public PdfView(String report, Collection<?> data, String filename) {
    super();
    this.setContentType("application/pdf");
    this.report = report;
    this.data = data;
    this.filename = filename;
  }

  @Override
  protected void renderMergedOutputModel(
      Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    // IEの場合はContent-Lengthヘッダが指定されていないとダウンロードが失敗するので、
    // サイズを取得するための一時的なバイト配列ストリームにコンテンツを書き出すようにする
    val baos = createTemporaryOutputStream();

    // 帳票レイアウト
    val report = loadReport();

    // データの設定
    val dataSource = new JRBeanCollectionDataSource(this.data);
    val print = JasperFillManager.fillReport(report, model, dataSource);

    val exporter = new JRPdfExporter();
    exporter.setExporterInput(new SimpleExporterInput(print));
    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
    exporter.exportReport();

    // ファイル名に日本語を含めても文字化けしないようにUTF-8にエンコードする
    val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
    val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
    response.setHeader(CONTENT_DISPOSITION, contentDisposition);

    writeToResponse(response, baos);
  }

  /**
   * 帳票レイアウトを読み込む
   *
   * @return
   */
  protected final JasperReport loadReport() {
    val resource = new ClassPathResource(this.report);

    try {
      val fileName = resource.getFilename();
      if (fileName.endsWith(".jasper")) {
        try (val is = resource.getInputStream()) {
          return (JasperReport) JRLoader.loadObject(is);
        }
      } else if (fileName.endsWith(".jrxml")) {
        try (val is = resource.getInputStream()) {
          JasperDesign design = JRXmlLoader.load(is);
          return JasperCompileManager.compileReport(design);
        }
      } else {
        throw new IllegalArgumentException(
            ".jasper または .jrxml の帳票を指定してください。 [" + fileName + "] must end in either ");
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("failed to load report. " + resource, e);
    } catch (JRException e) {
      throw new IllegalArgumentException("failed to parse report. " + resource, e);
    }
  }
}
