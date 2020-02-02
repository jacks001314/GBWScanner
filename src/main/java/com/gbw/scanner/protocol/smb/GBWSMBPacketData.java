package com.gbw.scanner.protocol.smb;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWPacketData;
import com.gbw.scanner.protocol.GBWProtoBuffer;

public abstract class GBWSMBPacketData<H extends GBWSMBHeader> implements GBWPacketData<GBWProtoBuffer> {

    private H header;
    protected GBWProtoBuffer dataBuffer;

    public GBWSMBPacketData(H header, byte[] data) throws GBWBuffer.GBWBufferException {
        this.header = header;
        this.dataBuffer = new GBWProtoBuffer(data);
        readHeader();
    }

    protected void readHeader() throws GBWBuffer.GBWBufferException {
        this.header.readFrom(dataBuffer);
    }

    public H getHeader() {
        return header;
    }

    @Override
    public GBWProtoBuffer getDataBuffer() {
        return dataBuffer;
    }

}
