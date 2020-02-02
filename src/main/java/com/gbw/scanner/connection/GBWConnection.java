package com.gbw.scanner.connection;

import java.io.IOException;

public  class GBWConnection implements Connection {

    protected final SocketClient socketClient;
    protected GBWInputStream  reader;
    protected GBWOutputStream writer;

    public GBWConnection(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.reader = new GBWInputStream(socketClient.getInput());
        this.writer = new GBWOutputStream(socketClient.getOutput());
    }

    @Override
    public void send(byte[] data) throws IOException {

        writer.write(data);
        writer.flush();
    }

    @Override
    public void send(byte[] data, int offset, int len) throws IOException {

        writer.write(data,offset,len);
        writer.flush();
    }

    @Override
    public void send(String data) throws IOException {

        writer.write(data.getBytes());
        writer.flush();
    }

    @Override
    public String readLine() throws IOException {

        return reader.readLine();
    }

    @Override
    public void read(byte[] data, int offset, int size) throws IOException {
        reader.read(data,offset,size);
    }

    @Override
    public void read(byte[] data) throws IOException {

        reader.read(data,0,data.length);
    }

    @Override
    public void close() throws IOException {

        socketClient.disconnect();
    }


}
