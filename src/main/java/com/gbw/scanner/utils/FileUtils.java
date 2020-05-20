package com.gbw.scanner.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

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

    public static final String getFileName(String fpath){

        Path path = Paths.get(fpath);
        return path.getFileName().toString();
    }

    public static final void mkDirs(String path){

        File file = new File(path);
        if(!file.exists())
            file.mkdirs();
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

    public static final void setExe(String fpath){

        try {
            Path path = Paths.get(fpath);
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            Files.setPosixFilePermissions(path, permissions);

        }catch (Exception e){

        }

    }
}
