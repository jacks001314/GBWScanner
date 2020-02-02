package com.gbw.scanner.protocol.smb;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWPacket;
import com.gbw.scanner.protocol.GBWProtoBuffer;

public abstract class GBWSMBPacket<D extends GBWSMBPacketData<H>, H extends GBWSMBHeader> implements GBWPacket<GBWProtoBuffer> {

    protected H header;

    public GBWSMBPacket(H header) {
        this.header = header;
    }

    public H getHeader() {
        return header;
    }

    protected abstract void read(D packetData) throws GBWBuffer.GBWBufferException;

    @Override
    public final void read(GBWProtoBuffer buffer) throws GBWBuffer.GBWBufferException {
        throw new GBWBuffer.GBWBufferException("Call read(D extends PacketData<H>) instead of this method");
    }

}
