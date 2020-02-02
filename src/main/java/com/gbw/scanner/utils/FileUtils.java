package com.gbw.scanner.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static final Path createPath(String fpath) throws IOException {

        Path path = Paths.get(fpath);

        if(!Files.exists(path)){

            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }

        return path;
    }
}
