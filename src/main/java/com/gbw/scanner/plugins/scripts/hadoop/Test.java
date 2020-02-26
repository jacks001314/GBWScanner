package com.gbw.scanner.plugins.scripts.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.Records;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

public class Test {

    private static boolean amCompleted;
    private static final long AM_STATE_WAIT_TIMEOUT_MS = 10000;
    private static void launchAM(ApplicationAttemptId attemptId)
            throws IOException, YarnException {


        ArrayList<String> envAMList = new ArrayList<String>();


        ContainerId containerId = ContainerId.newContainerId(attemptId, 0);

        envAMList.add(ApplicationConstants.Environment.USER.name()+"="+"dr.who");
        envAMList.add(ApplicationConstants.Environment.CONTAINER_ID.name() + "=" + containerId);
        envAMList.add(ApplicationConstants.Environment.NM_HOST.name() + "=" + "lala");
        envAMList.add(ApplicationConstants.Environment.NM_HTTP_PORT.name() + "=0");
        envAMList.add(ApplicationConstants.Environment.NM_PORT.name() + "=0");
        envAMList.add(ApplicationConstants.Environment.LOCAL_DIRS.name() + "= /tmp");
        envAMList.add(ApplicationConstants.APP_SUBMIT_TIME_ENV + "="
                + System.currentTimeMillis());

        String[] envAM = new String[envAMList.size()];
        Process amProc = Runtime.getRuntime().exec("whoami", envAMList.toArray(envAM));

        final BufferedReader errReader =
                new BufferedReader(new InputStreamReader(
                        amProc.getErrorStream(), Charset.forName("UTF-8")));
        final BufferedReader inReader =
                new BufferedReader(new InputStreamReader(
                        amProc.getInputStream(), Charset.forName("UTF-8")));

        // read error and input streams as this would free up the buffers
        // free the error stream buffer
        Thread errThread = new Thread() {
            @Override
            public void run() {
                try {
                    String line = errReader.readLine();
                    while((line != null) && !isInterrupted()) {
                        System.err.println(line);
                        line = errReader.readLine();
                    }
                } catch(IOException ioe) {
                    System.out.println("Error reading the error stream"+ioe.getMessage());
                }
            }
        };
        Thread outThread = new Thread() {
            @Override
            public void run() {
                try {
                    String line = inReader.readLine();
                    while((line != null) && !isInterrupted()) {
                        System.out.println(line);
                        line = inReader.readLine();
                    }
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        try {
            errThread.start();
            outThread.start();
        } catch (IllegalStateException ise) { }

        // wait for the process to finish and check the exit code
        try {
            int exitCode = amProc.waitFor();
            System.out.println("AM process exited with value: " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            amCompleted = true;
        }

        try {
            // make sure that the error thread exits
            // on Windows these threads sometimes get stuck and hang the execution
            // timeout and join later after destroying the process.
            errThread.join();
            outThread.join();
            errReader.close();
            inReader.close();
        } catch (InterruptedException ie) {

        } catch (IOException ioe) {

        }
        amProc.destroy();
    }

    public static void main(String[] args) throws IOException, YarnException, InterruptedException {

        amCompleted = false;
        YarnConfiguration yarnConf = new YarnConfiguration(new Configuration());

        yarnConf.set(YarnConfiguration.RM_ADDRESS, "101.124.62.41");
        YarnClient client = YarnClient.createYarnClient();


        client.init(yarnConf);
        client.start();


        ApplicationSubmissionContext appContext = client.createApplication()
                .getApplicationSubmissionContext();
        ApplicationId appId = appContext.getApplicationId();


        appContext.setApplicationName("LaTest");

        System.out.println(appId);
        // Set the priority for the application master

        // Set up the container launch context for the application master
        ContainerLaunchContext amContainer = Records
                .newRecord(ContainerLaunchContext.class);
        appContext.setAMContainerSpec(amContainer);

        // unmanaged AM
       // appContext.setUnmanagedAM(true);

        Map<String,String> env = new HashMap<>();
        StringBuilder classpath = new StringBuilder(ApplicationConstants.Environment.CLASSPATH.$()+"./*");

        for(String cp:yarnConf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH)){
            classpath.append(File.pathSeparator);
            classpath.append(cp);
        }
        env.put("CLASSPATH",classpath.toString());
        amContainer.setEnvironment(env);

        List<String> cmdList = new ArrayList<>();
        cmdList.add("touch /tmp/test.data");

        amContainer.setCommands(cmdList);

        Priority priority = Records.newRecord(Priority.class);
        priority.setPriority(1);
        appContext.setPriority(priority);
        appContext.setQueue("default");

        Resource resource = Records.newRecord(Resource.class);
        resource.setMemorySize(1024);
        resource.setVirtualCores(1);

        appContext.setResource(resource);
        client.submitApplication(appContext);

        while(true){

            Thread.sleep(1000);

            ApplicationReport report = client.getApplicationReport(appId);
            YarnApplicationState state = report.getYarnApplicationState();
            FinalApplicationStatus finalStatus = report.getFinalApplicationStatus();

            if(YarnApplicationState.FINISHED == state){
                if(FinalApplicationStatus.SUCCEEDED == finalStatus){
                    System.out.println("ok!");
                }else{
                    System.out.println("error");
                }
            }else if(YarnApplicationState.KILLED == state||YarnApplicationState.FAILED == state){
                System.out.println("Failed!");
            }
        }

    }


}
