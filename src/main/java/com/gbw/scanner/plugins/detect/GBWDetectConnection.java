package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.GBWOPUtils;
import com.gbw.scanner.utils.GBWPreOPUtils;
import com.xmap.api.utils.Text;
import com.xmap.api.utils.TextUtils;

import java.io.IOException;

public class GBWDetectConnection {

    private static byte[] getWriteData(GBWDetectDataWriter writer){

        if(writer.isBin())
            return ByteDataUtils.parseHex(writer.getData());

        return writer.getData().getBytes();
    }

    private static byte[] readData(Connection connection,GBWDetectDataReader reader) throws IOException {

        int len = reader.getLen();
        if(len<=0)
            len = 1024;

        byte[] res = new byte[len];

        connection.read(res);
        return res;
    }

    private static String readString(Connection connection,GBWDetectDataReader reader) throws IOException {

        int len = reader.getLen();
        if(len<=0)
            len = 1024;

        byte[] res = new byte[len];

        connection.read(res);

        return Text.decode(res);
    }

    private static String readLine(Connection connection,GBWDetectDataReader reader) throws IOException {

        return connection.readLine();
    }



    public boolean isMatch(Connection connection,GBWDetectRule rule){

        GBWDetectDataWriter detectDataWriter;
        GBWDetectDataReader detectDataReader;

        byte[] dataBytes = null;
        String dataString = null;
        boolean readBin = true;

        String rop = null;
        for(GBWDetectRuleData ruleData:rule.getRuleDatas()){

            detectDataWriter = ruleData.getDataWriter();
            detectDataReader = ruleData.getDataReader();

            if(!TextUtils.isEmpty(detectDataWriter.getData())){

                try {
                    connection.send(getWriteData(detectDataWriter));
                } catch (IOException e) {
                    return false;
                }
            }

            rop = detectDataReader.getRop();
            if(!TextUtils.isEmpty(rop)){

                try{
                    if(rop.equals("readBytes")){
                        dataBytes = readData(connection,detectDataReader);
                        readBin = true;
                    }else if(rop.equals("readString")){
                        dataString = readString(connection,detectDataReader);
                        readBin = false;
                    }else if(rop.equals("readLine")){
                        dataString = readString(connection,detectDataReader);
                        readBin = false;
                    }else{
                        return false;
                    }

                }catch (IOException e){

                    return false;
                }
            }

            if(readBin&&GBWOPUtils.isException(dataBytes))
                return false;
            if(!readBin&&GBWOPUtils.isException(dataString))
                return false;

            String afterString = null;
            byte[] afterBytes = null;
            boolean afterBin = true;

            /*start to process preOP*/
            String preOP = ruleData.getPreOP();
            if(TextUtils.isEmpty(preOP)){
                afterString = dataString;
                afterBytes = dataBytes;
                afterBin = readBin;
            }else{
                /*need pre operations*/
                if(readBin){
                    /*read bytes*/
                    if(preOP.equals("toBase64")){
                        afterString = GBWPreOPUtils.toBase64(dataBytes);
                        afterBin = false;
                    }else{
                        /*unknown preop*/
                        afterBytes = dataBytes;
                        afterBin = true;
                    }
                } else{
                    /*read string*/
                    if(preOP.equals("toLower")){
                        afterString = GBWPreOPUtils.toLower(dataString);
                        afterBin = false;
                    }else if(preOP.equals("toUPPer")){
                        afterString = GBWPreOPUtils.toUpper(dataString);
                        afterBin = false;
                    }else if(preOP.equals("toBase64")){

                        afterString = GBWPreOPUtils.toBase64(dataString);
                        afterBin = false;
                    }else if(preOP.equals("b64ToStr")){

                        afterString = GBWPreOPUtils.b64ToStr(dataString);
                        afterBin = false;
                    }else if(preOP.equals("b64ToBytes")){
                        afterBytes = GBWPreOPUtils.b64ToByte(dataString);
                        afterBin = true;
                    }else {
                     /*unknown preOP*/
                     afterString = dataString;
                     afterBin = false;
                    }
                }
            }

            /*start to match*/
            boolean match;
            if(afterBin){
                match = GBWOPUtils.isMatch(afterBytes,ruleData.getOp(),ruleData.getRawData());
            }else{
                match = GBWOPUtils.isMatch(afterString,ruleData.getOp(),ruleData.getRawData());
            }

            if(rule.isAnd()){
                if(!match)
                    return false;
            }else{
                if(match)
                    return true;
            }
        }

        return rule.isAnd()?true:false;
    }


}
