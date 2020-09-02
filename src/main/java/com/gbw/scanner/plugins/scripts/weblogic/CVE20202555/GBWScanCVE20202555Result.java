package com.gbw.scanner.plugins.scripts.weblogic.CVE20202555;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanCVE20202555Result extends GBWScanScriptResult {

    private String[] cmds;
    private String version;

    public GBWScanCVE20202555Result(GBWScanScriptCommonConfig scanScriptConfig, Host host) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.weblogicCVE20202555);

    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        super.makeDetails(cb);
        cb.field("cmds", cmds);
        cb.field("version",version);

        return cb;
    }

    public String[] getCmds() {
        return cmds;
    }

    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
