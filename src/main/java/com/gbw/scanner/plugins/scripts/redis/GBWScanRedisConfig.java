package com.gbw.scanner.plugins.scripts.redis;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

import java.util.List;

public class GBWScanRedisConfig extends GBWScanScriptCommonConfig {

    private boolean checkDir;
    private String key;
    private List<String> dirs;

    public boolean isCheckDir() {
        return checkDir;
    }

    public void setCheckDir(boolean checkDir) {
        this.checkDir = checkDir;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
