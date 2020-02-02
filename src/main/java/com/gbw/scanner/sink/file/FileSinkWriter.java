package com.gbw.scanner.sink.file;

import com.gbw.scanner.sink.es.ESIndexable;

import java.io.IOException;

public interface FileSinkWriter {

    void write(String data) throws IOException;

    void writeLine(String data) throws IOException;

    void writeLine(ESIndexable esIndexable) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;

}
