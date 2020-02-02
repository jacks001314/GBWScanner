package com.gbw.scanner.connection;

import java.io.IOException;

public interface Connection {

    void send(byte[] data) throws IOException;
    void send(byte[] data,int offset,int len) throws IOException;
    void send(String data) throws IOException;

    String readLine() throws IOException;
    void read(byte[] data) throws IOException;
    void read(byte[] data,int offset,int size) throws IOException;
    void close() throws IOException;
}
