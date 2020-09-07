package com.gbw.scanner.server.scan;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.weblogic.CVE20202555.GBWScanCVE20202555Config;
import com.gbw.scanner.plugins.scripts.weblogic.CVE20202555.GBWScanCVE20202555Result;
import com.gbw.scanner.utils.GsonUtils;
import com.xmap.api.utils.TextUtils;

import java.io.IOException;
import java.util.Arrays;

public class GBWScanResultFactory {

    private static final String scanConfigPath = "/opt/scan/GBWScanner/conf/scanScript.json";
    private static  GBWScanScriptConfig config =null;

    public static final GBWScannerResult make(GBWScanResultServerData serverData)  {

        if(config == null){
            try {
                config = GsonUtils.loadConfigFromJsonFile(scanConfigPath,GBWScanScriptConfig.class);
            } catch (IOException e) {
                return null;
            }
        }

        String type = serverData.getType();

        if(TextUtils.isEmpty(type))
            return null;

        Host host = new Host(serverData.getIp(),serverData.getIp(),serverData.getPort(), Arrays.asList(type),serverData.getProto());

        if(type.equals(GBWScanScriptPlugin.weblogicCVE20202555)){

            GBWScanCVE20202555Config cve20202555Config = config.getWeblogicCVE20202555Config();
            GBWScanCVE20202555Result result = new GBWScanCVE20202555Result(cve20202555Config,host);
            result.setCmds(cve20202555Config.getCmds());
            result.setVersion(serverData.getPayload());
            return result;
        }


        return null;
    }
}
