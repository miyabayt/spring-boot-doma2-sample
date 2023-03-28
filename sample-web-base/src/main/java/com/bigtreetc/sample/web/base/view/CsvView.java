package com.bigtreetc.sample.web.base.view;

import static com.bigtreetc.sample.common.util.ValidateUtils.isNotEmpty;
import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import lombok.val;
import org.springframework.web.servlet.view.AbstractView;

/** CSVビュー */
public class CsvView extends AbstractView {

  protected static final CsvMapper csvMapper = createCsvMapper();

  protected Class<?> clazz;

  protected Collection<?> data;

  @Setter protected String filename;

  @Setter protected List<String> columns;

  /**
   * CSVマッパーを生成する。
   *
   * @return
   */
  static CsvMapper createCsvMapper() {
    CsvMapper mapper = new CsvMapper();
    mapper.configure(ALWAYS_QUOTE_STRINGS, true);
    mapper.findAndRegisterModules();
    return mapper;
  }

  /**
   * コンストラクタ
   *
   * @param clazz
   * @param data
   * @param filename
   */
  public CsvView(Class<?> clazz, Collection<?> data, String filename) {
    setContentType("application/octet-stream; charset=Windows-31J;");
    this.clazz = clazz;
    this.data = data;
    this.filename = filename;
  }

  @Override
  protected boolean generatesDownloadContent() {
    return true;
  }

  @Override
  protected final void renderMergedOutputModel(
      Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    // ファイル名に日本語を含めても文字化けしないようにUTF-8にエンコードする
    val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
    val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);

    response.setHeader(CONTENT_TYPE, getContentType());
    response.setHeader(CONTENT_DISPOSITION, contentDisposition);

    // CSVヘッダをオブジェクトから作成する
    CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

    if (isNotEmpty(columns)) {
      // カラムが指定された場合は、スキーマを再構築する
      val builder = schema.rebuild().clearColumns();
      for (String column : columns) {
        builder.addColumn(column);
      }
      schema = builder.build();
    }

    // 書き出し
    val outputStream = createTemporaryOutputStream();
    try (Writer writer = new OutputStreamWriter(outputStream, "Windows-31J")) {
      csvMapper.writer(schema).writeValue(writer, data);
    }

    writeToResponse(response, outputStream);
  }
}
