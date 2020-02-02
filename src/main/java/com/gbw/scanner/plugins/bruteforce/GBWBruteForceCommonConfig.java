package com.gbw.scanner.plugins.bruteforce;

public class GBWBruteForceCommonConfig {

    private boolean isOn;
    private String userFName;
    private String passwdFName;
    private int timeout;

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
