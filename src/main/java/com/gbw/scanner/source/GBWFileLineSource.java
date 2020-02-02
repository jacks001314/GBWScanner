package com.gbw.scanner.source;

import com.gbw.scanner.Host;
import com.xmap.api.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GBWFileLineSource extends GBWAbstractHostSource {

    private final GBWFileLineSourceConfig config;
    private final BufferedReader reader;
    private final boolean containsPort;
    private boolean isReadAll;

    public GBWFileLineSource(GBWFileLineSourceConfig config) throws IOException {
        super(config);
        this.config = config;
        this.reader = Files.newBufferedReader(Paths.get(config.getHostFile()));
        List<Integer> ports = config.getPorts();
        this.containsPort = ports == null || ports.size() == 0;
        this.isReadAll = false;
    }

    public void preRead() {
    }

    public int read() {
        if (isReadAll) {
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
                    if (containsPort) {
                        String[] splits = nline.split(":");
                        Host host = new Host(splits[0], splits[0], Integer.parseInt(splits[1]), config.getScanType(),config.getProto());
                        put(host);
                    } else {
                        config.getPorts().forEach((port) -> {
                            put(new Host(nline, nline, port, config.getScanType(),config.getProto()));
                        });
                    }
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
}
