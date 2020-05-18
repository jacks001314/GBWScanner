package com.gbw.scanner.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static final Path createPath(String fpath) throws IOException
    {
        return createPath(fpath,true);
    }

    public static final Path createPath(String fpath,boolean isCreateFile) throws IOException {

        Path path = Paths.get(fpath);

        if(!Files.exists(path)){

            Files.createDirectories(path.getParent());

            if(isCreateFile)
                Files.createFile(path);
        }

        return path;
    }

    public static final void write(String fname,byte[] data) throws IOException {

        Files.write(createPath(fname,true),data);
    }

    public static final void delete(String fname) throws IOException {

        Files.delete(Paths.get(fname));
    }

    public static final void copy(String src,String dst) throws IOException {

        Files.copy(Paths.get(src),createPath(dst,false));
    }

    public static final void move(String src,String dst) throws IOException {

        Files.move(Paths.get(src),createPath(dst,false));
    }


}
