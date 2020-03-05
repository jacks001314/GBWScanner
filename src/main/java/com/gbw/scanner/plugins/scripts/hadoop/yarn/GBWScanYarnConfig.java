package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

import java.util.List;

public class GBWScanYarnConfig extends GBWScanScriptCommonConfig {

    private boolean runCmd;
    private boolean monitor;

    private String user;

    private String appname;

    /*Application Priority. Default 0*/
    private int priority;

    /*RM Queue in which this application is to be submitted*/
    private String queue;

    /*Application timeout in milliseconds*/
    private int timeout;

    /*Shell commands to be executed by*/
    private List<String> cmds;

    private List<GBWYarnEnv> envs;

    /*Amount of memory in MB to be requested to run the shell command*/
    private long memory;

    /*Amount of virtual cores to be requested to run the shell command"*/
    private int vcores;

    private String uri;
    private List<String> keys;



    public String getUri() {
        return uri;
    }



    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }



    public List<GBWYarnEnv> getEnvs() {
        return envs;
    }

    public void setEnvs(List<GBWYarnEnv> envs) {
        this.envs = envs;
    }


    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public int getVcores() {
        return vcores;
    }

    public void setVcores(int vcores) {
        this.vcores = vcores;
    }

    public boolean isRunCmd() {
        return runCmd;
    }

    public void setRunCmd(boolean runCmd) {
        this.runCmd = runCmd;
    }

    public List<String> getCmds() {
        return cmds;
    }

    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }

    public boolean isMonitor() {
        return monitor;
    }

    public void setMonitor(boolean monitor) {
        this.monitor = monitor;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
