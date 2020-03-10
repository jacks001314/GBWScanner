package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

import java.util.ArrayList;
import java.util.List;

public class GBWScanYarnConfig extends GBWScanScriptCommonConfig {

    private boolean runCmd;
    private boolean monitor;
    private boolean preFilter;

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

    private int ipcRetries;

    /*for utils*/
    private String addr;

    private int port;

    private String ua;

    public GBWScanYarnConfig(){

        setConTimeout(10000);
        setReadTimeout(10000);
        setOn(true);
        setUser("test");
        setRunCmd(false);
        setMonitor(false);
        setAppname("test");
        setPriority(0);
        setQueue("default");
        setTimeout(60000);
        List<String> cmd = new ArrayList<>();
        cmd.add("touch /tmp/test.data");

        setCmds(cmd);

        setEnvs(new ArrayList<>());
        setMemory(1024);
        setVcores(1);
        setUri("/cluster");
        List<String> keys = new ArrayList<>();
        keys.add("/cluster/nodes");
        keys.add("All Applications");
        setKeys(keys);
        setIpcRetries(0);
        setPreFilter(false);
        setPort(8088);
        setUa("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
    }

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

    public int getIpcRetries() {
        return ipcRetries;
    }

    public void setIpcRetries(int ipcRetries) {
        this.ipcRetries = ipcRetries;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String toString(){

        StringBuffer sb = new StringBuffer();
        cmds.forEach(e->sb.append(e+"\n"));

        return String.format("addr:%s\n" +
                        "port:%d\n" +
                        "appname:%s\n" +
                        "user:%s\n" +
                        "queue:%s\n" +
                        "priority:%d\n" +
                        "memory:%d\n" +
                        "cores:%d\n" +
                        "timeout:%d\n" +
                        "ipcRetries:%d\n" +
                        "mon:%s\n" +
                        "cmds:%s\n",
                addr==null?"0.0.0.0":addr,
                port,
                appname,
                user,
                queue,
                priority,
                memory,
                vcores,
                timeout,
                ipcRetries,
                monitor?"true":"false",
                sb.toString());

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isPreFilter() {
        return preFilter;
    }

    public void setPreFilter(boolean preFilter) {
        this.preFilter = preFilter;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
