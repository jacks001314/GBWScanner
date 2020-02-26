package com.gbw.scanner.plugins.scripts.web.tomcat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWAJPBodyRequest {

    private static void end(GBWAJPMessage message,int data_direction) {

        int len = message.getPos();
        byte[] buf = message.getBuffer();
        int dLen = len - 4;

        if(data_direction == GBWAJPConstants.SERVER_TO_CONTAINER){
            buf[0] = (byte) 0x12;
            buf[1] = (byte) 0x34;
        }else{
            buf[0] = (byte) 0x41;
            buf[1] = (byte) 0x42;
        }

        buf[2] = (byte) ((dLen>>>8) & 0xFF);
        buf[3] = (byte) (dLen & 0xFF);
    }

    public static GBWAJPMessage createMessage(String fname,int data_direction) throws IOException {

        byte[] data = Files.readAllBytes(Paths.get(fname));

        GBWAJPMessage message = new GBWAJPMessage(4+data.length+32);
        message.reset();

        if(data.length == 0){
            message.appendByte(0x12);
            message.appendByte(0x34);
            message.appendInt(0x00);
            return message;
        }

        message.appendBytes(data,0,data.length);
        end(message,data_direction);
        return message;
    }

}
