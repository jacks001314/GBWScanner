package com.gbw.scanner.plugins.scripts.weblogic.vuls;


import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWSpringJndi;

public class GBWCVE_2018_3191 extends GBWAbstractNoEchoVul{

    public GBWCVE_2018_3191(GBWScanWeblogicConfig config) {
        super(config, "CVE_2018_3191", "CVE_2018_3191");
        noEchoPayloads.add(new GBWSpringJndi());
    }


    @Override
    public String getDefaultProto() {
        return "iiop";
    }
}
