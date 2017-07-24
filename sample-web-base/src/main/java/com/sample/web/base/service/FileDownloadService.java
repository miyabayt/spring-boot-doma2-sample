package com.sample.web.base.service;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sample.domain.exception.FileNotFoundException;
import com.sample.domain.service.BaseService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ファイルダウンロード
 */
@Component
@Slf4j
public class FileDownloadService extends BaseService {

    private static final Tika TIKA = new Tika();

    /**
     * ファイルダウンロード用のレスポンスを作成します。
     *
     * @param resource
     * @param isAttachment
     * @return
     */
    public ResponseEntity<Resource> createResponseEntity(Resource resource, boolean isAttachment) {
        try {
            val inlineOrAttachment = (isAttachment) ? "attachment" : "inline";
            val contentDisposition = String.format("%s; filename=\"%s\"", inlineOrAttachment, resource.getFilename());

            val file = resource.getFile();
            val contentType = TIKA.detect(file);
            val contentLength = resource.contentLength();
            val lastModified = resource.lastModified();

            val response = ResponseEntity.ok().contentLength(contentLength)
                    .contentType(MediaType.parseMediaType(contentType)).lastModified(lastModified)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
            return response;

        } catch (IOException e) {
            throw new FileNotFoundException(e);
        }
    }
}
