package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient4;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient5;

public class GBWCVE_2018_3245 extends GBWAbstractNoEchoVul{

    public GBWCVE_2018_3245(GBWScanWeblogicConfig config) {

        super(config,"CVE_2018_3245","CVE_2018_3245");


        noEchoPayloads.add(new GBWJRMPClient4());
        noEchoPayloads.add(new GBWJRMPClient5());
    }

    @Override
    public String getDefaultProto() {
        return "t3";
    }

}
