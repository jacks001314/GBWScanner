package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GBWYarnAppPostData {

    @SerializedName("application-id")
    private String appId;

    @SerializedName("application-name")
    private String appname;

    @SerializedName("am-container-spec")
    private ContainerSpec containerSpec;

    private Resource resource;

    @SerializedName("unmanaged-AM")
    private boolean  unmanagedAM;

    @SerializedName("max-app-attempts")
    private int maxAppAttempts;

    @SerializedName("application-type")
    private String  appType;

    public GBWYarnAppPostData(GBWScanYarnConfig config,GBWYarnApp app){

        appId = app.getAppId();
        appname = config.getAppname();
        containerSpec = new ContainerSpec(config);
        resource = new Resource(config);
        unmanagedAM = false;
        maxAppAttempts = 2;
        appType = "YARN";
    }

    public String getAppType() {
        return appType;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public ContainerSpec getContainerSpec() {
        return containerSpec;
    }

    public void setContainerSpec(ContainerSpec containerSpec) {
        this.containerSpec = containerSpec;
    }

    public void setMaxAppAttempts(int maxAppAttempts) {
        this.maxAppAttempts = maxAppAttempts;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setUnmanagedAM(boolean unmanagedAM) {
        this.unmanagedAM = unmanagedAM;
    }

    public boolean isUnmanagedAM() {
        return unmanagedAM;
    }

    public Resource getResource() {
        return resource;
    }

    public String getAppname() {
        return appname;
    }

    public int getMaxAppAttempts() {
        return maxAppAttempts;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    private class Resource {

        private long memory;
        private int vCores;

        public Resource(GBWScanYarnConfig config){

            memory = config.getMemory();

            vCores = config.getVcores();
        }

        public long getMemory() {
            return memory;
        }

        public void setMemory(long memory) {
            this.memory = memory;
        }

        public int getvCores() {
            return vCores;
        }

        public void setvCores(int vCores) {
            this.vCores = vCores;
        }
    }

    private class ContainerSpec{

        private Commands commands;
        private Environment environment;


        public ContainerSpec(GBWScanYarnConfig config){

            commands = new Commands(config);
            environment = new Environment(config);
        }

        public Commands getCommands() {
            return commands;
        }

        public void setCommands(Commands commands) {
            this.commands = commands;
        }

        public Environment getEnvironment() {
            return environment;
        }

        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }

    private class Commands {

        private String command;

        public Commands(GBWScanYarnConfig config){

            StringBuffer sb = new StringBuffer();

            config.getCmds().forEach(cmd->sb.append(cmd+" "));
            command = sb.toString();
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    private class Environment
    {
        private List<GBWYarnEnv> entry;

        public Environment(GBWScanYarnConfig config){

            entry = config.getEnvs();

            if(entry.size() == 0){
                GBWYarnEnv env = new GBWYarnEnv();
                env.setKey("test");
                env.setValue("yarn");
                entry.add(env);
            }
        }

        public List<GBWYarnEnv> getEntry() {
            return entry;
        }

        public void setEntry(List<GBWYarnEnv> entry) {
            this.entry = entry;
        }
    }
}
