package com.gbw.scanner.source.file;

import com.gbw.scanner.Host;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.xmap.api.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GBWFileLineSource implements GBWHostSource {

    private final GBWFileLineSourceConfig config;
    private  BufferedReader reader;
    private final boolean containsPort;
    private boolean isReadAll;

    public GBWFileLineSource(GBWFileLineSourceConfig config) throws IOException {

        this.config = config;
        List<Integer> ports = config.getPorts();
        this.containsPort = ports == null || ports.size() == 0;
        this.isReadAll = false;
    }


    @Override
    public void open() throws Exception {

        reader = Files.newBufferedReader(Paths.get(config.getHostFile()));
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
                    if (containsPort) {
                        String[] splits = nline.split(":");

                        Host host = null;
                        if(splits.length == 2){
                            host = new Host(splits[0], splits[0], Integer.parseInt(splits[1]), config.getScanType(),config.getProto());
                        }else if(splits.length == 3) {

                            List<String> types = new ArrayList<>();
                            types.add(splits[2]);
                            host = new Host(splits[0], splits[0], Integer.parseInt(splits[1]),types, config.getProto());
                        }else if(splits.length == 4){

                            List<String> types = new ArrayList<>();
                            types.add(splits[2]);
                            host = new Host(splits[0], splits[0], Integer.parseInt(splits[1]),types,splits[3]);
                        }

                        if(host!=null)
                            sourcePool.put(host);

                    } else {
                        config.getPorts().forEach((port) -> {
                            sourcePool.put(new Host(nline, nline, port, config.getScanType(),config.getProto()));
                        });
                    }
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
            Files.deleteIfExists(Paths.get(config.getHostFile()));

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
}
