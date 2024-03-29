package com.gbw.scanner.source.fofa;

import com.gbw.scanner.Host;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWFoFaSource implements GBWHostSource {

    private GBWFoFaSourceConfig config;
    private BufferedReader reader;
    private boolean isReadAll;

    public GBWFoFaSource(GBWFoFaSourceConfig config) throws IOException {
        this.config = config;
        this.isReadAll = false;
    }

    public void preRead() {
    }

    private FofaJsonData readJsonData(String line) {
        Gson gson = new Gson();
        FofaJsonData jsonData = (FofaJsonData) gson.fromJson(line, FofaJsonData.class);
        jsonData.init(config.getProto(),config.getPort());
        return jsonData;
    }

    @Override
    public void open() throws Exception {

        reader = Files.newBufferedReader(Paths.get(config.getFofaFile()));
    }

    @Override
    public int read(GBWHostSourcePool sourcePool) {

        if (isReadAll) {
            return 0;
        } else {
            try {
                String line = reader.readLine();
                if (line == null) {
                    isReadAll = true;
                    return 0;
                }

                String nline = line.trim();
                if (!TextUtils.isEmpty(nline)) {
                    FofaJsonData jsonData = readJsonData(nline);
                    Host host = new Host(jsonData.getHost(), jsonData.getHost(), jsonData.getPort(), config.getScanType(), jsonData.getProto());
                    sourcePool.put(host);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 1;
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
            Files.deleteIfExists(Paths.get(config.getFofaFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRemove() {
        return true;
    }

    @Override
    public boolean isTimeout(long curTime) {
        return true;
    }

    @Override
    public void reopen(long curTime) throws Exception {

    }

    private class FofaJsonData {

        private String id;
        private String host;
        private String proto;
        private int port;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;

        }

        public void init(String defProto,int defPort){

            String[] ids = id.replace("//","").split(":");

            this.proto = defProto;
            this.port = defPort;

            if(ids.length == 1){
                this.host = ids[0];
            }else if(ids.length == 2){

                char c = ids[0].charAt(0);
                if(Character.isDigit(c)){
                    this.host = ids[0];
                    this.port = Integer.parseInt(ids[1]);
                }else{
                    this.proto = ids[0];
                    this.host = ids[1];
                    if(proto.equalsIgnoreCase("https")){
                        port = 443;
                    }else if(proto.equalsIgnoreCase("http")){
                        port = 80;
                    }
                }
            }else if(ids.length == 3){

                this.proto = ids[0];
                this.host = ids[1];
                this.port = Integer.parseInt(ids[2]);
            }

        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getProto() {
            return proto;
        }

        public void setProto(String proto) {
            this.proto = proto;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String toString(){

            return String.format("id:%s,proto:%s,host:%s,%d",id,proto,host,port);
        }
    }

}
