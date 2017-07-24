package com.sample.web.base.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sample.common.util.EncodeUtils;
import com.sample.domain.service.BaseService;

import lombok.val;

/**
 * CSVダウロード
 */
@Service
public class CsvDownloadService extends BaseService {

    protected static final CsvMapper csvMapper = new CsvMapper();

    /**
     * ファイルダウンロード用のレスポンスを作成します。
     * 
     * @param clazz
     * @param list
     * @param filename
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<String> createResponseEntity(Class<T> clazz, List<T> list, String filename) {
        // CSV変換
        val csvBody = getCsvBodyAsString(clazz, list);

        // RFC 5987
        val encodedFilename = EncodeUtils.encodeUtf8(filename);
        val contentDisposition = String.format("attachment; filename*=UTF-8''\"%s\"", encodedFilename);

        val response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=Windows-31J;")
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(csvBody);

        return response;
    }

    /**
     * 引数に指定したリストをCSVに変換して文字列で返します。
     * 
     * @param clazz
     * @param list
     * @param <T>
     * @return
     */
    protected <T> String getCsvBodyAsString(Class<T> clazz, List<T> list) {
        String csvBody = "";

        // CSVヘッダをオブジェクトから作成する
        CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();

        try {
            csvBody = csvMapper.writer(schema).writeValueAsString(list);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return csvBody;
    }
}
