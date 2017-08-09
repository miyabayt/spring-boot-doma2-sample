package com.sample.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    /**
     * ディレクトリがない場合は作成します。
     * 
     * @param location
     */
    public static void createPathIfNessesary(Path location) {
        try {
            if (!Files.isDirectory(location)) {
                Files.createDirectory(location);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
        }
    }
}
