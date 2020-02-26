package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.connection.Connection;

import java.io.IOException;

public class GBWAJPResponse {

    private int magic;
    private int dlen;
    private int code;
    private byte[] data;

    public GBWAJPResponse(Connection connection,int maxLen) throws IOException {

        byte[] h = new byte[5];
        connection.read(h,0,5);
        magic = readInt(h,0);
        dlen = readInt(h,2);
        code = h[4];

        if(dlen<0||dlen>maxLen||code<3||code>6){
            throw new IOException("Ivalid ajp message!");
        }
        if(dlen>0){

            data = new byte[dlen];
            connection.read(data,0,dlen);
        }
    }


    private int readInt(byte[] b,int pos) {
        int val = (b[pos] & 0xFF ) << 8;
        val += b[pos+1] & 0xFF;
        return val;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getDlen() {
        return dlen;
    }

    public void setDlen(int dlen) {
        this.dlen = dlen;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
