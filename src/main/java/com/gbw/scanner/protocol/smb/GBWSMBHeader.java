package com.gbw.scanner.protocol.smb;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWProtoBuffer;

public interface GBWSMBHeader {

    void writeTo(GBWProtoBuffer buffer);

    void readFrom(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException;
}
