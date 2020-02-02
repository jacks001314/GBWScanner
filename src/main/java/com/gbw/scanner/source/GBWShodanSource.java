package com.gbw.scanner.source;

import com.gbw.scanner.Host;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWShodanSource extends GBWAbstractHostSource {

    private GBWShodanSourceConfig config;
    private final BufferedReader reader;
    private boolean isReadAll;

    public GBWShodanSource(GBWShodanSourceConfig config) throws IOException {
        super(config);
        this.config = config;
        this.reader = Files.newBufferedReader(Paths.get(config.getShodanFile()));
        this.isReadAll = false;
    }

    public void preRead() {
    }

    private JsonData readJsonData(String line) {
        Gson gson = new Gson();
        JsonData jsonData = (JsonData)gson.fromJson(line, JsonData.class);
        return jsonData;
    }

    public int read() {
        if (this.isReadAll) {
            return 0;
        } else {
            try {
                String line = reader.readLine();
                if (line == null) {
                    isReadAll = true;
                    readEnd();
                    return 0;
                }

                String nline = line.trim();
                if (!TextUtils.isEmpty(nline)) {
                    JsonData jsonData = readJsonData(nline);
                    Host host = new Host(jsonData.getIp_str(), jsonData.getIp_str(), jsonData.getPort(), config.getScanType(), config.getProto());
                    put(host);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 1;
        }
    }

    public void readEnd() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class JsonData {
        private String ip_str;
        private int port;

        private JsonData() {
        }

        public String getIp_str() {
            return this.ip_str;
        }

        public void setIp_str(String ip_str) {
            this.ip_str = ip_str;
        }

        public int getPort() {
            return this.port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String toString() {
            return String.format("%s:%d", this.ip_str, this.port);
        }
    }

}
