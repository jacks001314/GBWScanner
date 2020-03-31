package com.gbw.scanner.plugins.bruteforce;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class GBWBruteForceCommonConfig {

    private boolean isOn;
    private String userFName;
    private String passwdFName;
    private int timeout;

    public void addOpts(Options opts){

        opts.addOption("user",true,"user name file");
        opts.addOption("passwd",true,"passwd file");
        opts.addOption("timeout",true,"connect/read time out(mills)");

    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{

        isOn = true;
        timeout = 10000;
        if(!cmdLine.hasOption("user")||!cmdLine.hasOption("passwd")){

            throw new IllegalArgumentException("Must specify users File and passwds File!");
        }

        userFName = cmdLine.getOptionValue("user");
        passwdFName = cmdLine.getOptionValue("passwd");

        if(cmdLine.hasOption("timeout")){
            timeout = Integer.parseInt(cmdLine.getOptionValue("timeout"));
        }

    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getUserFName() {
        return userFName;
    }

    public void setUserFName(String userFName) {
        this.userFName = userFName;
    }

    public String getPasswdFName() {
        return passwdFName;
    }

    public void setPasswdFName(String passwdFName) {
        this.passwdFName = passwdFName;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
