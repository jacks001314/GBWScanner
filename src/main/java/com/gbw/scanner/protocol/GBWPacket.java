package com.gbw.scanner.protocol;

public interface GBWPacket<B extends GBWBuffer<B>> {

    void write(B buffer);

    void read(B buffer) throws GBWBuffer.GBWBufferException;
}
