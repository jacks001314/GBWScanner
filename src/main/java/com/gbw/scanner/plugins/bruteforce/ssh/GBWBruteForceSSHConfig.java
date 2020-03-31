package com.gbw.scanner.plugins.bruteforce.ssh;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class GBWBruteForceSSHConfig extends GBWBruteForceCommonConfig {

    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPasswd;

    public void addOpts(Options opts){

        super.addOpts(opts);
        opts.addOption("proxyHost",true,"ftp proxy host");
        opts.addOption("proxyPort",true,"ftp proxy port");
        opts.addOption("proxyUser",true,"ftp proxy user");
        opts.addOption("proxyPasswd",true,"ftp proxy passwd");

    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{

        super.initFromOpts(cmdLine);

        proxyHost = "";
        proxyPort = 0;
        proxyUser = "";
        proxyPasswd = "";


        if(cmdLine.hasOption("proxyHost"))
            proxyHost = cmdLine.getOptionValue("proxyHost");

        if(cmdLine.hasOption("proxyPort"))
            proxyPort = Integer.parseInt(cmdLine.getOptionValue("proxyPort"));
        if(cmdLine.hasOption("proxyUser"))
            proxyUser = cmdLine.getOptionValue("proxyUser");

        if(cmdLine.hasOption("proxyPasswd"))
            proxyPasswd = cmdLine.getOptionValue("proxyPasswd");


    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPasswd() {
        return proxyPasswd;
    }

    public void setProxyPasswd(String proxyPasswd) {
        this.proxyPasswd = proxyPasswd;
    }

}
