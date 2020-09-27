package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWPayload10271;
import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWHttpEchoShell;
import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWHttpEchoShell12;

public class GBWCVE_2017_10271 extends GBWXmlDeserailVul {

    public GBWCVE_2017_10271(GBWScanWeblogicConfig config){

        super(config,"CVE_2017_10271","CVE_2017_10271");
        payloads.add(new GBWPayload10271(GBWHttpEchoShell.class));
        payloads.add(new GBWPayload10271(GBWHttpEchoShell12.class));
    }


}
