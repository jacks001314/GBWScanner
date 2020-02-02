package com.gbw.scanner.plugins.scripts.windows.smb.MS17010;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanSMBMS17010ScriptResult extends GBWScanScriptResult {

    private String os;
    private boolean doublePulsar;
    private long key;

    public GBWScanSMBMS17010ScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.ms17010Scan);
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("os",os);
        cb.field("doublePulsar",doublePulsar);
        cb.field("key",key);

        return cb;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public boolean isDoublePulsar() {
        return doublePulsar;
    }

    public void setDoublePulsar(boolean doublePulsar) {
        this.doublePulsar = doublePulsar;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }
}
