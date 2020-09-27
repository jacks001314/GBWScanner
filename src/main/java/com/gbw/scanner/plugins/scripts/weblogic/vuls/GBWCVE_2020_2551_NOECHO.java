package com.gbw.scanner.plugins.scripts.weblogic.vuls;


import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJRMPClient;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWSpringJndi;

public class GBWCVE_2020_2551_NOECHO extends GBWAbstractNoEchoVul {

    public GBWCVE_2020_2551_NOECHO(GBWScanWeblogicConfig config){

        super(config,"CVE_2020_2551","CVE_2020_2551");
        noEchoPayloads.add(new GBWSpringJndi());
        noEchoPayloads.add(new GBWJRMPClient());

    }

    @Override
    public String getDefaultProto() {
        return "iiop";
    }
}
