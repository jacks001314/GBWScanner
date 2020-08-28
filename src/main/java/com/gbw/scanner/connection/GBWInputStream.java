package com.gbw.scanner.connection;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GBWInputStream extends FilterInputStream {

    protected final byte[] buf;
    protected int count;
    protected int limit;

    public GBWInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        } else {
            this.buf = new byte[size];
        }
    }

    public GBWInputStream(InputStream in) {
        this(in, 8192);
    }

    public byte readByte() throws GBWConnectionException {
        this.ensureFill();
        return this.buf[this.count++];
    }

    public String readLine() throws GBWConnectionException {
        StringBuilder sb = new StringBuilder();
        this.ensureFill();

        while(this.count!=this.limit) {

            byte b = this.buf[this.count++];
            if (b == 13) {
                //this.ensureFill();
                byte c = this.buf[this.count++];
                if (c == 10) {
                    String reply = sb.toString();
                    if (reply.length() == 0) {
                        return "";
                    }
                    return reply;
                }
                sb.append((char)b);
                sb.append((char)c);
            }else if(b == 10){
                return sb.toString();
            }else {
                sb.append((char)b);
            }
        }
        return sb.toString();
    }

    public byte[] readLineBytes() throws GBWConnectionException {
        this.ensureFill();
        int pos = this.count;
        byte[] buf = this.buf;

        while(pos != this.limit) {
            if (buf[pos++] == 13) {
                if (pos == this.limit) {
                    return this.readLineBytesSlowly();
                }

                if (buf[pos++] == 10) {
                    int N = pos - this.count - 2;
                    byte[] line = new byte[N];
                    System.arraycopy(buf, this.count, line, 0, N);
                    this.count = pos;
                    return line;
                }
            }
        }

        return this.readLineBytesSlowly();
    }

    private byte[] readLineBytesSlowly() throws GBWConnectionException {
        ByteArrayOutputStream bout = null;

        while(true) {
            while(true) {
                this.ensureFill();
                byte b = this.buf[this.count++];
                if (b == 13) {
                    this.ensureFill();
                    byte c = this.buf[this.count++];
                    if (c == 10) {
                        return bout == null ? new byte[0] : bout.toByteArray();
                    }

                    if (bout == null) {
                        bout = new ByteArrayOutputStream(16);
                    }

                    bout.write(b);
                    bout.write(c);
                } else {
                    if (bout == null) {
                        bout = new ByteArrayOutputStream(16);
                    }

                    bout.write(b);
                }
            }
        }
    }

    public int read(byte[] b, int off, int len) throws GBWConnectionException {
        this.ensureFill();
        int length = Math.min(this.limit - this.count, len);
        System.arraycopy(this.buf, this.count, b, off, length);
        this.count += length;
        return length;
    }

    private void ensureFill() throws GBWConnectionException {
        if (this.count >= this.limit) {
            try {
                this.limit = this.in.read(this.buf);
                this.count = 0;
                if (this.limit == -1) {
                    throw new GBWConnectionException("Unexpected end of stream.");
                }
            } catch (IOException var2) {
                throw new GBWConnectionException(var2.getMessage());
            }
        }

    }
}
