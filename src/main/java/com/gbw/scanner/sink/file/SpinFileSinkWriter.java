package com.gbw.scanner.sink.file;

import com.gbw.scanner.sink.es.ESIndexable;
import com.gbw.scanner.utils.FileUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.FileWriter;
import java.io.IOException;

public class SpinFileSinkWriter implements FileSinkWriter {

    private final FileWriter fileWriter;

    public SpinFileSinkWriter(FileSinkConfig config,int n) throws IOException {

        String fpath = String.format("%s/%d/%s",config.getRootDir(),n,config.getPrefix());

        FileUtils.createPath(fpath);

        fileWriter = new FileWriter(fpath,true);
    }

    @Override
    public void write(String data) throws IOException {
        fileWriter.write(data);
    }

    @Override
    public void writeLine(String data) throws IOException {
        fileWriter.write(data);
        fileWriter.write("\n");
    }

    @Override
    public void writeLine(ESIndexable esIndexable) throws IOException {

        XContentBuilder cb = XContentFactory.jsonBuilder();
        cb.startObject();
        esIndexable.dataToJson(cb);
        cb.endObject();

        writeLine(cb.string());
    }

    @Override
    public void flush() throws IOException {
        fileWriter.flush();
    }

    @Override
    public void close() throws IOException {
        fileWriter.close();
    }


}
