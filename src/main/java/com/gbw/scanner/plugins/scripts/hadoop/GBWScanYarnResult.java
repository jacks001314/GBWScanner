package com.gbw.scanner.plugins.scripts.hadoop;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;

public class GBWScanYarnResult extends GBWScanScriptResult {

    public GBWScanYarnResult(GBWScanScriptCommonConfig scanScriptConfig, Host host, String type) {
        super(scanScriptConfig, host, type);
    }
}
