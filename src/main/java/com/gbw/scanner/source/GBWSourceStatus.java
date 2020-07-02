package com.gbw.scanner.source;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GBWSourceStatus {

    private long tv;
    private long lastCheckTime;

    private String statusFPath;


    public GBWSourceStatus(String statusFileName,long tv) throws Exception {

        this.tv = tv;
        this.statusFPath = statusFileName;

        Path path = Paths.get(this.statusFPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            this.lastCheckTime = 0L;
        } else {
            List<String> lines = Files.readAllLines(path);
            if (lines != null && lines.size() != 0) {
                this.lastCheckTime = Long.parseLong((String) lines.get(0));
            } else {
                this.lastCheckTime = 0L;
            }
        }

    }

    public void updateStatusTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.statusFPath));
            writer.write(Long.toString(lastCheckTime));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isTimeout(long curTime){

        return curTime-lastCheckTime>=tv;
    }

    public long getTv() {
        return tv;
    }

    public void setTv(long tv) {
        this.tv = tv;
    }

    public long getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
}
