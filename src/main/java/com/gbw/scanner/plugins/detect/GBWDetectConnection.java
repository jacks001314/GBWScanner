package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.rule.GBWRuleItem;
import com.gbw.scanner.rule.GBWRuleSourceEntry;
import com.gbw.scanner.utils.ByteDataUtils;
import com.xmap.api.utils.Text;
import com.xmap.api.utils.TextUtils;

import java.io.IOException;

public class GBWDetectConnection implements GBWRuleSourceEntry {

    private final Connection connection;

    public GBWDetectConnection(Connection connection) {

        this.connection = connection;

    }


    private  byte[] getWriteData(GBWDetectDataWriter writer){

        if(writer.isBin())
            return ByteDataUtils.parseHex(writer.getData());

        return writer.getData().getBytes();
    }

    private byte[] readData(GBWDetectDataReader reader) throws IOException {

        int len = reader.getLen();
        if(len<=0)
            len = 1024;

        byte[] res = new byte[len];

        connection.read(res);
        return res;
    }

    private String readString(GBWDetectDataReader reader) throws IOException {

        int len = reader.getLen();
        if(len<=0)
            len = 1024;

        byte[] res = new byte[len];

        connection.read(res);

        return Text.decode(res);
    }

    private String readLine(GBWDetectDataReader reader) throws IOException {

        return connection.readLine();
    }

    @Override
    public boolean canMatch(String proto) {
        return true;
    }

    @Override
    public String getTargetValue(GBWRuleItem item) {


        GBWDetectDataWriter detectDataWriter = item.getDataWriter();
        GBWDetectDataReader detectDataReader = item.getDataReader();

        if(!TextUtils.isEmpty(detectDataWriter.getData())){

            try {
                connection.send(getWriteData(detectDataWriter));
            } catch (IOException e) {
                return "";
            }
        }

        String rop = detectDataReader.getRop();
        if(!TextUtils.isEmpty(rop)){

            try{

                if(rop.equals("readString")){
                    String dataString = readString(detectDataReader);
                    return item.isBin()?ByteDataUtils.toHex(dataString):dataString;
                }else if(rop.equals("readLine")){
                    String line = readLine(detectDataReader);
                    return item.isBin()?ByteDataUtils.toHex(line):line;
                }else{
                    /*readBytes*/
                    byte[] dataBytes = readData(detectDataReader);
                    return item.isBin()?ByteDataUtils.toHex(dataBytes):Text.decode(dataBytes);
                }

            }catch (IOException e){

                return "";
            }
        }

        return "";

    }
}
