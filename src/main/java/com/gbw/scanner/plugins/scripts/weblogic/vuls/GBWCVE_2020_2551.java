package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCoherencePayload;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCommonsCollections3;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCommonsCollections7;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJdk7u21;

public class GBWCVE_2020_2551 extends GBWAbstractEchoVul {


    public GBWCVE_2020_2551(GBWScanWeblogicConfig config) throws Exception {
        super(config,"CVE-2020-2551", "CVE-2020-2551");

        echoPayloads.add(new GBWCommonsCollections3());
        echoPayloads.add(new GBWCommonsCollections7());
        echoPayloads.add(new GBWJdk7u21());
        echoPayloads.add(new GBWCoherencePayload(config));
    }


    @Override
    public String getDefaultProto() {
        return "iiop";
    }

}

