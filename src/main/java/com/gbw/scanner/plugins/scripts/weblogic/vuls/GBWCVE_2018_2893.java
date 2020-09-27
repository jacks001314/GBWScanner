package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient3;

public class GBWCVE_2018_2893 extends GBWAbstractNoEchoVul{

    public GBWCVE_2018_2893(GBWScanWeblogicConfig config) {
        super(config, "CVE_2018_2893", "CVE_2018_2893");
        noEchoPayloads.add(new GBWJRMPClient3());
    }


    @Override
    public String getDefaultProto() {
        return "t3";
    }
}
