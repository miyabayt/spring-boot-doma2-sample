package com.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.sample.common.util.EncodeUtils;

import lombok.val;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * PDFビュー
 */
public class PdfView extends JasperReportsPdfView {

    protected String filename;

    /**
     * コンストラクタ
     *
     * @param location
     * @param filename
     */
    public PdfView(String location, String filename) {
        super();
        this.filename = filename;

        setUrl(location);
        setReportDataKey("data");
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    @Override
    protected void renderReportUsingOutputStream(net.sf.jasperreports.engine.JRExporter exporter,
            JasperPrint populatedReport, HttpServletResponse response) throws Exception {

        // IEの場合はContent-Lengthヘッダが指定されていないとダウンロードが失敗するので、
        // サイズを取得するための一時的なバイト配列ストリームにコンテンツを書き出すようにする
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        JasperReportsUtils.render(exporter, populatedReport, baos);

        // ファイル名に日本語を含めても文字化けしないようにUTF-8にエンコードする
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''\"%s\"", encodedFilename);
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);

        writeToResponse(response, baos);
    }

    @Override
    protected JasperReport getReport() {
        JasperReport report = super.getReport();
        if (report == null) {
            report = loadReport();
        }

        return report;
    }

    protected JasperReport loadReport() {
        String url = getUrl();
        if (url == null) {
            return null;
        }
        Resource mainReport = new ClassPathResource(getUrl());
        return loadReport(mainReport);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setHeaders(new Properties());
        super.renderMergedOutputModel(model, request, response);
    }

    @Override
    protected boolean isContextRequired() {
        return false;
    }
}
