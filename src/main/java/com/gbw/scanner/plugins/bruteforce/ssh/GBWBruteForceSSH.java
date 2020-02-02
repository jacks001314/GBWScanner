package com.gbw.scanner.plugins.bruteforce.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.HTTPProxyData;
import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.*;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWBruteForceSSH extends GBWAbstractBruteForce {

    private static final Logger log = LoggerFactory.getLogger(GBWBruteForceSSH.class);
    private GBWBruteForceSSHConfig sshConfig;

    public GBWBruteForceSSH(GBWBruteForceSSHConfig config) throws IOException, InterruptedException {
        super(config);
        sshConfig = config;
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry) {

        String server = host.getServer();
        int port = host.getPort();

        if(TextUtils.isEmpty(server)){
            log.error("Please specify this host!");
            return null;
        }

        Connection connection;

        if(TextUtils.isEmpty(sshConfig.getProxyHost())){

            connection = new Connection(server,port);
        }else{

            HTTPProxyData httpProxyData = new HTTPProxyData(sshConfig.getProxyHost(),sshConfig.getProxyPort(),
                    sshConfig.getProxyUser(),sshConfig.getProxyPasswd());
            connection = new Connection(server,port,httpProxyData);
        }

        try {
            ConnectionInfo connectionInfo = connection.connect(null,sshConfig.getTimeout(),0);

            if(connection.authenticateWithPassword(entry.getUser(),entry.getPasswd())){
                return new GBWBruteForceResult(entry,host,GBWBruteForcePlugin.BRUTEFORCESSH);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }finally {

            connection.close();
        }

        return null;
    }

}
