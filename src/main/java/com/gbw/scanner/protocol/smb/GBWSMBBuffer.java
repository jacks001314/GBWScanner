package com.gbw.scanner.protocol.smb;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWEndian;
import com.gbw.scanner.utils.Charsets;

import java.util.Arrays;

public class GBWSMBBuffer extends GBWBuffer<GBWSMBBuffer> {

    private static final byte[] RESERVED_2 = new byte[]{0x0, 0x0};
    private static final byte[] RESERVED_4 = new byte[]{0x0, 0x0, 0x0, 0x0};

    public GBWSMBBuffer() {
        super(GBWEndian.LE);
    }

    public GBWSMBBuffer(byte[] data) {
        super(data, GBWEndian.LE);
    }

    /**
     * Puts '0' bytes for reserved parts of messages/headers
     *
     * @param length The length of the reserved space.
     * @return this
     */
    public GBWBuffer<GBWSMBBuffer> putReserved(int length) {
        byte[] nullBytes = new byte[length];
        Arrays.fill(nullBytes, (byte) 0);
        putRawBytes(nullBytes);
        return this;
    }

    /**
     * Shortcut method for putting 1 reserved byte in the buffer.
     *
     * @return this
     */
    public GBWBuffer<GBWSMBBuffer> putReserved1() {
        putByte((byte) 0);
        return this;
    }

    /**
     * Shortcut method for putting 2 reserved bytes in the buffer.
     *
     * @return this
     */
    public GBWBuffer<GBWSMBBuffer> putReserved2() {
        putRawBytes(RESERVED_2);
        return this;
    }

    /**
     * Shortcut method for putting 4 reserved bytes in the buffer.
     *
     * @return this
     */
    public GBWBuffer<GBWSMBBuffer> putReserved4() {
        putRawBytes(RESERVED_4);
        return this;
    }

    /**
     * [MS-SMB2].pdf 2.2 Message Syntax
     *
     * @param string The string value to write
     * @return this
     */
    public GBWBuffer<GBWSMBBuffer> putString(String string) {
        return putString(string, Charsets.UTF_16);
    }

    /**
     * [MS-SMB2].pdf 2.2 Message Syntax
     *
     * @param string the string of which to write the length
     * @return this
     */
    public GBWBuffer<GBWSMBBuffer> putStringLengthUInt16(String string) {
        if (string == null) {
            return putUInt16(0);
        }
        return putUInt16(string.length() * 2);
    }
}
