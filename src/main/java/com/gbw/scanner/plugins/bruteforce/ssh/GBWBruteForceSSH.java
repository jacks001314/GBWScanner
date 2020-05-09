package com.gbw.scanner.plugins.bruteforce.ssh;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.GBWAbstractBruteForce;
import com.gbw.scanner.plugins.bruteforce.GBWBruteForcePlugin;
import com.gbw.scanner.plugins.bruteforce.GBWBruteForceResult;
import com.gbw.scanner.plugins.bruteforce.GBWDictEntry;
import com.xmap.api.utils.TextUtils;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GBWBruteForceSSH extends GBWAbstractBruteForce {

    private static final Logger log = LoggerFactory.getLogger(GBWBruteForceSSH.class);
    private GBWBruteForceSSHConfig sshConfig;

    public GBWBruteForceSSH(GBWBruteForceSSHConfig config) throws IOException, InterruptedException {
        super(config);
        sshConfig = config;
    }

    private boolean isPass(GBWBruteForceResult result){

        String cmdResult = result.getCmdResult();

        if(TextUtils.isEmpty(cmdResult)){
            if(sshConfig.isIgnoreEmpty())
                return true;

            return false;
        }

        List<String> keys = sshConfig.getPassKeys();

        if(keys == null ||keys.size()==0)
            return false;

        for(String key:keys){

            if(cmdResult.contains(key))
                return true;
        }

        return false;
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry) {

        GBWBruteForceResult result = null;
        String server = host.getServer();
        int port = host.getPort();

        if (TextUtils.isEmpty(server)) {
            log.error("Please specify this host!");
            return null;
        }

        String cmd = sshConfig.getCmd();

        SSHClient ssh = new SSHClient();

        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.setConnectTimeout(sshConfig.getTimeout());
        ssh.setTimeout(sshConfig.getTimeout());
        Session session = null;

        try {

            ssh.connect(server, port);
            ssh.authPassword(entry.getUser(), entry.getPasswd());
            /*ok*/
            result = new GBWBruteForceResult(entry, host, GBWBruteForcePlugin.BRUTEFORCESSH);
            if(!TextUtils.isEmpty(cmd)){
                try {
                    session = ssh.startSession();
                    Session.Command exe = session.exec(cmd);
                    result.setCmd(cmd);
                    result.setCmdResult(IOUtils.readFully(exe.getInputStream()).toString());
                }catch (Exception e){

                }
            }

            return isPass(result)?null:result;

        } catch (Exception e) {
           // e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                }
            }

            try {
                ssh.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

}
