package com.gbw.scanner.sink.file;

import java.io.IOException;

public class FileSinkWriterFactory {

    private FileSinkWriterFactory(){

    }

    public static FileSinkWriter create(FileSinkConfig config,int n) throws IOException {

        String type = config.getType();
        FileSinkWriter fileSinkWriter = null;

        if(type.equalsIgnoreCase("spin")){

            fileSinkWriter = new SpinFileSinkWriter(config,n);
        }

        return fileSinkWriter;
    }
}
