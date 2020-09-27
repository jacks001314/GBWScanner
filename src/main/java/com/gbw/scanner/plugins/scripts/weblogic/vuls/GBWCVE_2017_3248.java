package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient;

public class GBWCVE_2017_3248 extends GBWAbstractNoEchoVul{

    public GBWCVE_2017_3248(GBWScanWeblogicConfig config){

        super(config,"CVE_2017_3248","CVE_2017_3248");
        noEchoPayloads.add(new GBWJRMPClient());
    }

    @Override
    public String getDefaultProto() {
        return "t3";
    }
}
