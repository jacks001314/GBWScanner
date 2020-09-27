package com.gbw.scanner.plugins.scripts.weblogic.vuls;


import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.*;

public class GBWCVE_2019_2729 extends GBWXmlDeserailVul {

    public GBWCVE_2019_2729(GBWScanWeblogicConfig config) {

        super(config, "CVE_2019_2729", "CVE_2019_2729");

        payloads.add(new GBWUnitOfWorkChangeSet2729(new GBWCommonsCollections3()));
        payloads.add(new GBWUnitOfWorkChangeSet2729(new GBWCommonsCollections7()));
        payloads.add(new GBWUnitOfWorkChangeSet2729(new GBWJdk7u21()));
        payloads.add(new GBWEventData2729());
    }

}
