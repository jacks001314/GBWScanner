package com.gbw.scanner.plugins.scripts.web.tomcat;

import org.apache.coyote.ajp.AjpMessage;

public class GBWAJPMessage extends AjpMessage {


    public GBWAJPMessage(int packetSize) {
        super(packetSize);
    }

    public byte[] raw() {
        return buf;
    }

    public byte readByte() {
        return buf[pos++];
    }

    public int readInt() {
        int val = (buf[pos++] & 0xFF ) << 8;
        val += buf[pos++] & 0xFF;
        return val;
    }

    public String readString() {
        int len = readInt();
        return readString(len);
    }

    public String readString(int len) {
        StringBuilder buffer = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char c = (char) buf[pos++];
            buffer.append(c);
        }
        // Read end of string marker
        readByte();

        return buffer.toString();
    }

    public void appendString(String str) {

        if (str == null) {
            appendInt(0);
            appendByte(0);
        } else {
            int len = str.length();
            appendInt(len);

            for(int i = 0; i < len; ++i) {
                char c = str.charAt(i);
                if (c <= 31 && c != '\t' || c == 127 || c > 255) {
                    c = ' ';
                }

                appendByte(c);
            }

            appendByte(0);
        }
    }

    public int getPos(){
        return pos;
    }

    @Override
    public void reset() {
        super.reset();
    }

}
