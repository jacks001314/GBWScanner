package com.gbw.scanner.plugins.scripts.weblogic.vuls;


import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient1;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient2;

public class GBWCVE_2018_2628 extends GBWAbstractNoEchoVul {

    public GBWCVE_2018_2628(GBWScanWeblogicConfig config){

        super(config,"CVE_2018_2628","CVE_2018_2628");
        noEchoPayloads.add(new GBWJRMPClient1());
        noEchoPayloads.add(new GBWJRMPClient2());
    }


    @Override
    public String getDefaultProto() {
        return "t3";
    }
}
