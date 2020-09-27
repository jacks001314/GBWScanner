package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCommonsCollections3;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCommonsCollections7;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJdk7u21;

public class GBWCVE_2016_0638 extends GBWAbstractEchoVul {

    public GBWCVE_2016_0638(GBWScanWeblogicConfig config) throws Exception {
        super(config,"CVE-2016-0638", "CVE-2016-0638");

        echoPayloads.add(new GBWCommonsCollections3());
        echoPayloads.add(new GBWCommonsCollections7());
        echoPayloads.add(new GBWJdk7u21());

    }


    @Override
    public String getDefaultProto() {
        return "t3";
    }
}
