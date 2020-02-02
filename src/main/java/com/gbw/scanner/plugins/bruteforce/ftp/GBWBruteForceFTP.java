package com.gbw.scanner.plugins.bruteforce.ftp;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.GBWAbstractBruteForce;
import com.gbw.scanner.plugins.bruteforce.GBWBruteForcePlugin;
import com.gbw.scanner.plugins.bruteforce.GBWBruteForceResult;
import com.gbw.scanner.plugins.bruteforce.GBWDictEntry;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.util.TrustManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWBruteForceFTP extends GBWAbstractBruteForce {

    private static final Logger log = LoggerFactory.getLogger(GBWBruteForceFTP.class);

    private GBWBruteForceFTPConfig config;
    private FTPClientConfig ftpClientConfig;

    public GBWBruteForceFTP(GBWBruteForceFTPConfig config) throws IOException, InterruptedException {

        super(config);
        this.config = config;

        if(TextUtils.isEmpty(config.getServerType())){
            this.ftpClientConfig = new FTPClientConfig();
        }else{
            this.ftpClientConfig = new FTPClientConfig(config.getServerType());
        }

    }

    private FTPClient makeClient(Host host){

        FTPClient ftpClient = null;
        String proto = getProto(host);
        boolean isImplicit = config.isImplicit();

        if(TextUtils.isEmpty(proto)||proto.equals("ftp")){

            if(TextUtils.isEmpty(config.getProxyHost()))
                ftpClient = new FTPClient();
            else
                ftpClient = new FTPHTTPClient(config.getProxyHost(),config.getProxyPort(),config.getProxyUser(),config.getProxyPasswd());
        }else{

            FTPSClient ftps =new FTPSClient(proto, isImplicit);
            String trustmgr = config.getTrustmgr();

            if ("all".equals(trustmgr)) {
                ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
            } else if ("valid".equals(trustmgr)) {
                ftps.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
            } else if ("none".equals(trustmgr)||TextUtils.isEmpty(trustmgr)) {
                ftps.setTrustManager(null);
            }

            ftpClient = ftps;
        }

        ftpClient.setConnectTimeout(config.getTimeout());
        ftpClient.configure(ftpClientConfig);

        return ftpClient;
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry)  {

        String server = host.getServer();
        if(TextUtils.isEmpty(server)){
            log.warn("Please specify this ftp host!");
            return null;
        }
        FTPClient ftpClient = makeClient(host);

        if(ftpClient == null) {
            log.error("Cannot create ftpClient:"+server+":"+host.getPort());
            return null;
        }

        try{
            int reply;
            if(host.getPort()>0){
                ftpClient.connect(server,host.getPort());
            }else{
                ftpClient.connect(server);
            }
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                log.error("FTP server refused connection:"+server+":"+host.getPort());
                return null;
            }

        }catch (Exception e){

            if (ftpClient.isConnected())
            {
                try
                {
                    ftpClient.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            log.error("Could not connect to server:"+server+":"+host.getPort());
            return null;
        }

        try {
            if(ftpClient.login(entry.getUser(),entry.getPasswd())){

                /*ok*/
                log.info("Login ftp:"+server+":"+host.getPort()+","+entry.getUser()+","+entry.getPasswd());
                return new GBWBruteForceResult(entry,host, GBWBruteForcePlugin.BRUTEFORCEFTP);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                ftpClient.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
