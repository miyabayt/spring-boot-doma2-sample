package com.sample.web.base.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.sample.common.util.FileUtils;
import com.sample.domain.exception.FileNotFoundException;
import com.sample.domain.service.BaseService;

/**
 * ファイルアップロード
 */
@Service
public class FileUploadService extends BaseService {

    /**
     * ファイルの一覧を取得します。
     *
     * @param location
     * @return
     */
    public Stream<Path> listAllFiles(Path location) {
        try {
            FileUtils.createPathIfNessesary(location);

            return Files.walk(location, 1).filter(path -> !path.equals(location))
                    .map(path -> location.relativize(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to list uploadfiles. ", e);
        }
    }

    /**
     * ファイルを読み込みます。
     *
     * @param location
     * @param filename
     * @return
     */
    public Resource loadFile(Path location, String filename) {
        try {
            Path file = location.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }

            throw new FileNotFoundException("could not read file. " + filename);

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "malformed Url resource. [location=" + location.toString() + ", filename=" + filename + "]", e);
        }
    }

    /**
     * ファイルを保存します。
     * 
     * @param location
     * @param file
     *            保存先ディレクトリ
     */
    public void saveFile(Path location, MultipartFile file) {
        Assert.notNull(file, "file can't be null");
        String filename = file.getOriginalFilename();

        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("cloud not save empty file. " + filename);
            }

            FileUtils.createPathIfNessesary(location);

            Files.copy(file.getInputStream(), location.resolve(filename));

        } catch (IOException e) {
            throw new IllegalStateException("failed to save file. " + filename, e);
        }
    }
}
