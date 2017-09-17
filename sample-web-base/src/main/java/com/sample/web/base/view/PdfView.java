package com.sample.web.base.view;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.sample.common.util.EncodeUtils;

import lombok.val;
import net.sf.jasperreports.engine.JasperPrint;

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

        setUrl("classpath:" + location);
        setReportDataKey("data");
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    @Override
    protected void renderReportUsingOutputStream(net.sf.jasperreports.engine.JRExporter exporter,
            JasperPrint populatedReport, HttpServletResponse response) throws Exception {

        // IE workaround: write into byte array first.
        ByteArrayOutputStream baos = createTemporaryOutputStream();
        JasperReportsUtils.render(exporter, populatedReport, baos);

        // RFC 5987
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''\"%s\"", encodedFilename);
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);

        writeToResponse(response, baos);
    }
}
