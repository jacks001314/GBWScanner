package com.gbw.scanner.connection;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GBWOutputStream extends FilterOutputStream {

    protected final byte[] buf;
    protected int count;


    public GBWOutputStream(OutputStream out) {
        this(out, 8192);
    }

    public GBWOutputStream(OutputStream out, int size) {
        super(out);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        } else {
            this.buf = new byte[size];
        }
    }

    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.out.write(this.buf, 0, this.count);
            this.count = 0;
        }

    }

    public void write(byte b) throws IOException {
        if (this.count == this.buf.length) {
            this.flushBuffer();
        }

        this.buf[this.count++] = b;
    }

    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (len >= this.buf.length) {
            this.flushBuffer();
            this.out.write(b, off, len);
        } else {
            if (len >= this.buf.length - this.count) {
                this.flushBuffer();
            }

            System.arraycopy(b, off, this.buf, this.count, len);
            this.count += len;
        }

    }

    public void writeCrLf() throws IOException {
        if (2 >= this.buf.length - this.count) {
            this.flushBuffer();
        }

        this.buf[this.count++] = 13;
        this.buf[this.count++] = 10;
    }

    public void flush() throws IOException {
        this.flushBuffer();
        this.out.flush();
    }
}
