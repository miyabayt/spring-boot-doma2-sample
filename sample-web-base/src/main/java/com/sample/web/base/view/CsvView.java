package com.sample.web.base.view;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sample.common.util.EncodeUtils;

import lombok.val;

/**
 * CSVビュー
 */
public class CsvView extends AbstractView {

    protected Class<?> clazz;

    protected Collection<?> data;

    protected String filename;

    protected static final CsvMapper csvMapper = createCsvMapper();

    /**
     * CSVマッパーを生成する。
     *
     * @return
     */
    static CsvMapper createCsvMapper() {
        CsvMapper mapper = new CsvMapper();
        mapper.configure(ALWAYS_QUOTE_STRINGS, true);
        return mapper;
    }

    /**
     * コンストラクタ
     */
    public CsvView() {
        setContentType("application/octet-stream; charset=Windows-31J;");
    }

    public CsvView(Class<?> clazz, Collection<?> data, String filename) {
        this();

        this.clazz = clazz;
        this.data = data;
        this.filename = filename;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // RFC 5987
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''\"%s\"", encodedFilename);

        response.setHeader(CONTENT_TYPE, getContentType());
        response.setHeader(CONTENT_DISPOSITION, contentDisposition);

        // CSVヘッダをオブジェクトから作成する
        CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

        // 書き出し
        OutputStream outputStream = response.getOutputStream();
        try (Writer writer = new OutputStreamWriter(outputStream, "Windows-31J")) {
            csvMapper.writer(schema).writeValue(writer, data);
        }
    }
}