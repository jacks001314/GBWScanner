package com.gbw.scanner.plugins.scripts.weblogic.vuls;


import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.*;

public class GBWCVE_2019_2725 extends GBWXmlDeserailVul {

    public GBWCVE_2019_2725(GBWScanWeblogicConfig config) {

        super(config, "CVE_2019_2725", "CVE_2019_2725");
        payloads.add(new GBWUnitOfWorkChangeSet2725(new GBWCommonsCollections3()));
        payloads.add(new GBWUnitOfWorkChangeSet2725(new GBWCommonsCollections7()));
        payloads.add(new GBWUnitOfWorkChangeSet2725(new GBWJdk7u21()));
        payloads.add(new GBWEventData2725());
    }

}
