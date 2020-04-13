package com.gbw.scanner.plugins.scripts.redis;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWScanRedisResult  extends GBWScanScriptResult {

    private List<String> writableDirs;

    public GBWScanRedisResult(GBWScanScriptCommonConfig scanScriptConfig, Host host) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.redisScan);

        writableDirs = new ArrayList<>();

    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("writableDirs",writableDirs);

        return cb;
    }

    public void addDir(String dir){

        writableDirs.add(dir);
    }

    public List<String> getWritableDirs() {
        return writableDirs;
    }

    public void setWritableDirs(List<String> writableDirs) {
        this.writableDirs = writableDirs;
    }

    public String toString(){

        StringBuffer sb = new StringBuffer("[");

        writableDirs.forEach(e->sb.append(e+","));
        sb.append("]");
        
        return String.format("Find OK:Redis no auth bug,address:%s:%d,writable dirs:%s",getHost(),getPort(),sb.toString());
    }

}
