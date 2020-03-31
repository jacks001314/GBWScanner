package com.gbw.scanner.plugins.bruteforce.ftp;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class GBWBruteForceFTPConfig extends GBWBruteForceCommonConfig {

    private boolean isImplicit;
    private String trustmgr;
    private String serverType;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPasswd;

    public GBWBruteForceFTPConfig(){

        this.isImplicit = false;
        this.trustmgr = "";
        this.serverType = "";
    }

    public void addOpts(Options opts){

        super.addOpts(opts);

        opts.addOption("implicit",false,"implicit config");
        opts.addOption("trustmgr",true,"ftps trustmgr");
        opts.addOption("serverType",true,"ftp server type");
        opts.addOption("proxyHost",true,"ftp proxy host");
        opts.addOption("proxyPort",true,"ftp proxy port");
        opts.addOption("proxyUser",true,"ftp proxy user");
        opts.addOption("proxyPasswd",true,"ftp proxy passwd");


    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{

        super.initFromOpts(cmdLine);

        isImplicit = false;
        trustmgr = "";
        serverType = "";
        proxyHost = "";
        proxyPort = 0;
        proxyUser = "";
        proxyPasswd = "";

        if(cmdLine.hasOption("implicit"))
            isImplicit = true;

        if(cmdLine.hasOption("trustmgr"))
            trustmgr = cmdLine.getOptionValue("trustmgr");

        if(cmdLine.hasOption("serverType"))
            serverType = cmdLine.getOptionValue("serverType");

        if(cmdLine.hasOption("proxyHost"))
            proxyHost = cmdLine.getOptionValue("proxyHost");

        if(cmdLine.hasOption("proxyPort"))
            proxyPort = Integer.parseInt(cmdLine.getOptionValue("proxyPort"));
        if(cmdLine.hasOption("proxyUser"))
            proxyUser = cmdLine.getOptionValue("proxyUser");

        if(cmdLine.hasOption("proxyPasswd"))
            proxyPasswd = cmdLine.getOptionValue("proxyPasswd");


    }

    public boolean isImplicit() {
        return isImplicit;
    }

    public void setImplicit(boolean implicit) {
        isImplicit = implicit;
    }

    public String getTrustmgr() {
        return trustmgr;
    }

    public void setTrustmgr(String trustmgr) {
        this.trustmgr = trustmgr;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
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
