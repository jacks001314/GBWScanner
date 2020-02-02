package com.gbw.scanner.protocol;

import org.apache.commons.codec.Charsets;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public abstract class GBWEndian {

    private static final byte[] NULL_TERMINATOR = new byte[]{0, 0};

    public static final GBWEndian LE = new Little();
    public static final GBWEndian BE = new Big();

    <T extends GBWBuffer<T>> String readNullTerminatedUtf16String(GBWBuffer<T> buffer, Charset charset) throws  GBWBuffer.GBWBufferException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[2];
        buffer.readRawBytes(bytes);
        while (bytes[0] != 0 || bytes[1] != 0) {
            baos.write(bytes, 0, 2);
            buffer.readRawBytes(bytes);
        }
        return new String(baos.toByteArray(), charset);
    }

    <T extends GBWBuffer<T>> String readUtf16String(GBWBuffer<T> buffer, int length, Charset charset) throws GBWBuffer.GBWBufferException {
        byte[] stringBytes = new byte[length * 2];
        buffer.readRawBytes(stringBytes);
        return new String(stringBytes, charset);
    }

    <T extends GBWBuffer<T>> void writeNullTerminatedUtf16String(GBWBuffer<T> buffer, String string) {
        writeUtf16String(buffer, string);
        buffer.putRawBytes(NULL_TERMINATOR);
    }


    private static class Big extends GBWEndian {

        @Override
        public <T extends GBWBuffer<T>> void writeUInt16(GBWBuffer<T> buffer, int uint16) {
            if (uint16 < 0 || uint16 > 0xFFFF) {
                throw new IllegalArgumentException("Invalid uint16 value: " + uint16);
            }
            buffer.putRawBytes(new byte[]{(byte) (uint16 >> 8), (byte) uint16});
        }

        @Override
        public <T extends GBWBuffer<T>> int readUInt16(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            byte[] b = buffer.readRawBytes(2);
            return b[0] << 8 & 0xFF00 | b[1] & 0x00FF;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUInt24(GBWBuffer<T> buffer, int uint24) {
            if (uint24 < 0 || uint24 > 0xFFFFFF) {
                throw new IllegalArgumentException("Invalid uint24 value: " + uint24);
            }
            buffer.putRawBytes(new byte[]{(byte) (uint24 >> 16), (byte) (uint24 >> 8), (byte) uint24});
        }

        @Override
        public <T extends GBWBuffer<T>> int readUInt24(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            byte[] b = buffer.readRawBytes(3);
            return b[0] << 16 & 0xFF0000 |
                    b[1] << 8 & 0x00FF00 |
                    b[2] & 0x0000FF;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUInt32(GBWBuffer<T> buffer, long uint32) {
            if (uint32 < 0 || uint32 > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("Invalid uint32 value: " + uint32);
            }
            buffer.putRawBytes(new byte[]{
                    (byte) (uint32 >> 24),
                    (byte) (uint32 >> 16),
                    (byte) (uint32 >> 8),
                    (byte) uint32
            });
        }

        @Override
        public <T extends GBWBuffer<T>> long readUInt32(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            byte[] b = buffer.readRawBytes(4);
            return b[0] << 24 & 0xFF000000L |
                    b[1] << 16 & 0x00FF0000L |
                    b[2] << 8 & 0x0000FF00L |
                    b[3] & 0x000000FFL;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUInt64(GBWBuffer<T> buffer, long uint64) {
            if (uint64 < 0) {
                throw new IllegalArgumentException("Invalid uint64 value: " + uint64);
            }
            writeLong(buffer, uint64);
        }

        @Override
        public <T extends GBWBuffer<T>> long readUInt64(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            long uint64 = (readUInt32(buffer) << 32) + (readUInt32(buffer) & 0xFFFFFFFFL);
            if (uint64 < 0) {
                throw new GBWBuffer.GBWBufferException("Cannot handle values > " + Long.MAX_VALUE);
            }
            return uint64;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeLong(GBWBuffer<T> buffer, long longVal) {
            buffer.putRawBytes(new byte[]{
                    (byte) (longVal >> 56),
                    (byte) (longVal >> 48),
                    (byte) (longVal >> 40),
                    (byte) (longVal >> 32),
                    (byte) (longVal >> 24),
                    (byte) (longVal >> 16),
                    (byte) (longVal >> 8),
                    (byte) longVal});
        }

        @Override
        public <T extends GBWBuffer<T>> long readLong(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            long result = 0;
            byte[] b = buffer.readRawBytes(8);
            for (int i = 0; i < 8; i++) {
                result <<= 8;
                result |= (b[i] & 0xFF);
            }
            return result;
        }

        @Override
        public <T extends GBWBuffer<T>> String readUtf16String(GBWBuffer<T> buffer, int length) throws GBWBuffer.GBWBufferException {
            return readUtf16String(buffer, length, Charsets.UTF_16BE);
        }

        @Override
        public <T extends GBWBuffer<T>> String readNullTerminatedUtf16String(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            return readNullTerminatedUtf16String(buffer, Charsets.UTF_16BE);
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUtf16String(GBWBuffer<T> buffer, String string) {
            byte[] bytes = string.getBytes(Charsets.UTF_16BE);
            buffer.putRawBytes(bytes);
        }

        @Override
        public String toString() {
            return "big endian";
        }
    }

    private static class Little extends GBWEndian {

        @Override
        public <T extends GBWBuffer<T>> void writeUInt16(GBWBuffer<T> buffer, int uint16) {
            if (uint16 < 0 || uint16 > 0xFFFF) {
                throw new IllegalArgumentException("Invalid uint16 value: " + uint16);
            }
            buffer.putRawBytes(new byte[]{
                    (byte) uint16,
                    (byte) (uint16 >> 8)});
        }

        @Override
        public <T extends GBWBuffer<T>> int readUInt16(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            byte[] b = buffer.readRawBytes(2);
            return b[0] & 0x00FF |
                    b[1] << 8 & 0xFF00;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUInt24(GBWBuffer<T> buffer, int uint24) {
            if (uint24 < 0 || uint24 > 0xFFFFFF) {
                throw new IllegalArgumentException("Invalid uint24 value: " + uint24);
            }
            buffer.putRawBytes(new byte[]{
                    (byte) uint24,
                    (byte) (uint24 >> 8),
                    (byte) (uint24 >> 16)});

        }

        @Override
        public <T extends GBWBuffer<T>> int readUInt24(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            byte[] b = buffer.readRawBytes(3);
            return b[0] & 0x0000FF |
                    b[1] << 8 & 0x00FF00 |
                    b[2] << 16 & 0xFF0000;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUInt32(GBWBuffer<T> buffer, long uint32) {
            if (uint32 < 0 || uint32 > 0xFFFFFFFFL) {
                throw new IllegalArgumentException("Invalid uint32 value: " + uint32);
            }
            buffer.putRawBytes(new byte[]{
                    (byte) uint32,
                    (byte) (uint32 >> 8),
                    (byte) (uint32 >> 16),
                    (byte) (uint32 >> 24)});

        }

        @Override
        public <T extends GBWBuffer<T>> long readUInt32(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            byte[] b = buffer.readRawBytes(4);
            return b[0] & 0x000000FFL |
                    b[1] << 8 & 0x0000FF00L |
                    b[2] << 16 & 0x00FF0000L |
                    b[3] << 24 & 0xFF000000L;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUInt64(GBWBuffer<T> buffer, long uint64) {
            if (uint64 < 0) {
                throw new IllegalArgumentException("Invalid uint64 value: " + uint64);
            }
            writeLong(buffer, uint64);
        }

        @Override
        public <T extends GBWBuffer<T>> long readUInt64(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            long uint64 = (readUInt32(buffer) & 0xFFFFFFFFL) + (readUInt32(buffer) << 32);
            if (uint64 < 0) {
                throw new GBWBuffer.GBWBufferException("Cannot handle values > " + Long.MAX_VALUE);
            }
            return uint64;
        }

        @Override
        public <T extends GBWBuffer<T>> void writeLong(GBWBuffer<T> buffer, long longVal) {
            buffer.putRawBytes(new byte[]{
                    (byte) longVal,
                    (byte) (longVal >> 8),
                    (byte) (longVal >> 16),
                    (byte) (longVal >> 24),
                    (byte) (longVal >> 32),
                    (byte) (longVal >> 40),
                    (byte) (longVal >> 48),
                    (byte) (longVal >> 56)});
        }

        @Override
        public <T extends GBWBuffer<T>> long readLong(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            long result = 0;
            byte[] bytes = buffer.readRawBytes(8);
            for (int i = 7; i >= 0; i--) {
                result <<= 8;
                result |= (bytes[i] & 0xFF);
            }
            return result;

        }

        @Override
        public <T extends GBWBuffer<T>> String readUtf16String(GBWBuffer<T> buffer, int length) throws GBWBuffer.GBWBufferException {
            return readUtf16String(buffer, length, Charsets.UTF_16LE);
        }

        @Override
        public <T extends GBWBuffer<T>> String readNullTerminatedUtf16String(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException {
            return readNullTerminatedUtf16String(buffer, Charsets.UTF_16LE);
        }

        @Override
        public <T extends GBWBuffer<T>> void writeUtf16String(GBWBuffer<T> buffer, String string) {
            byte[] bytes = string.getBytes(Charsets.UTF_16LE);
            buffer.putRawBytes(bytes);
        }

        @Override
        public String toString() {
            return "little endian";
        }
    }

    public abstract <T extends GBWBuffer<T>> void writeUInt16(GBWBuffer<T> buffer, int uint16);

    public abstract <T extends GBWBuffer<T>> int readUInt16(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException;

    public abstract <T extends GBWBuffer<T>> void writeUInt24(GBWBuffer<T> buffer, int uint24);

    public abstract <T extends GBWBuffer<T>> int readUInt24(GBWBuffer<T> buffer) throws  GBWBuffer.GBWBufferException;

    public abstract <T extends GBWBuffer<T>> void writeUInt32(GBWBuffer<T> buffer, long uint32);

    public abstract <T extends GBWBuffer<T>> long readUInt32(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException;

    public abstract <T extends GBWBuffer<T>> void writeUInt64(GBWBuffer<T> buffer, long uint64);

    public abstract <T extends GBWBuffer<T>> long readUInt64(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException;

    public abstract <T extends GBWBuffer<T>> void writeLong(GBWBuffer<T> buffer, long longVal);

    public abstract <T extends GBWBuffer<T>> long readLong(GBWBuffer<T> buffer) throws  GBWBuffer.GBWBufferException;

    public abstract <T extends GBWBuffer<T>> void writeUtf16String(GBWBuffer<T> buffer, String string);

    public abstract <T extends GBWBuffer<T>> String readUtf16String(GBWBuffer<T> buffer, int length) throws GBWBuffer.GBWBufferException;

    public abstract <T extends GBWBuffer<T>> String readNullTerminatedUtf16String(GBWBuffer<T> buffer) throws GBWBuffer.GBWBufferException;
}
