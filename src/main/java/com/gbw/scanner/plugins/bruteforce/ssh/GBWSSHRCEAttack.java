package com.gbw.scanner.plugins.bruteforce.ssh;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.IOException;

public class GBWSSHRCEAttack {

    public static String runCmd(String host,int port,String user,String passwd,String cmd){

        String res = "";
        SSHClient ssh = new SSHClient();

        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.setConnectTimeout(100000);
        ssh.setTimeout(100000);
        Session session = null;

        try {
            ssh.connect(host, port);
            ssh.authPassword(user,passwd);
            /*ok*/
            session = ssh.startSession();
            Session.Command exe = session.exec(cmd);
            res = IOUtils.readFully(exe.getInputStream()).toString();

        }catch (Exception e){
            res = e.getMessage();
        }finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                }
            }
            try {
                ssh.disconnect();
            } catch (IOException e) {
            }
        }

        return res;
    }


}
