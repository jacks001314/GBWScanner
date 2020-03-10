package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.HttpUtils;
import com.google.gson.Gson;
import org.apache.commons.cli.*;
import org.apache.hadoop.yarn.api.records.ResourceInformation;
import org.apache.hadoop.yarn.util.UnitsConversionUtil;
import org.apache.hadoop.yarn.util.resource.ResourceUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GBWYarnUtils {


    public static GBWScanYarnConfig createYarnConfig(String[] args) throws ParseException {

        GBWScanYarnConfig config = new GBWScanYarnConfig();

        // Command line options
        Options opts = new Options();

        opts.addOption("addr", true, "Application address<ip>:[port]");
        opts.addOption("port", true, "Application port");
        opts.addOption("user", true, "Application user. Default value - test");
        opts.addOption("appname", true, "Application Name. Default value - test");
        opts.addOption("priority", true, "Application Priority. Default 0");
        opts.addOption("queue", true, "RM Queue in which this application is to be submitted");
        opts.addOption("timeout", true, "Application timeout in milliseconds");
        opts.addOption("cmd", true, "will run shell cmd,eg:touch,/tmp/test.data");
        opts.addOption("mon", false, "monitor the application status");
        opts.addOption("memory", true, "memory need be allocated for application");
        opts.addOption("cores", true, "cpu cores need be allocated for application");
        opts.addOption("ipcRetries", true, "the number of ipc retry when connection ipc failed");
        opts.addOption("ua", true, "http request user-agent");
        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWScanYarnUtil", opts);
            return null;
        }


        if(!cliParser.hasOption("addr")){
            System.err.println("Please Specify yarn resource manager address!");
            return null;
        }


        config.setAddr(cliParser.getOptionValue("addr"));

        if(cliParser.hasOption("port")){

            config.setPort(Integer.parseInt(cliParser.getOptionValue("port")));
        }

        if(cliParser.hasOption("user")){
            config.setUser(cliParser.getOptionValue("user"));
        }

        if(cliParser.hasOption("appname")){
            config.setAppname(cliParser.getOptionValue("appname"));
        }
        if(cliParser.hasOption("priority")){
            config.setPriority(Integer.parseInt(cliParser.getOptionValue("priority")));
        }
        if(cliParser.hasOption("queue")){
            config.setQueue(cliParser.getOptionValue("queue"));
        }

        if(cliParser.hasOption("timeout")){
            config.setTimeout(Integer.parseInt(cliParser.getOptionValue("timeout")));
        }

        if(cliParser.hasOption("cmd")){

            List<String> cmds = new ArrayList<>();
            String cmd = cliParser.getOptionValue("cmd").replace(","," ");

            cmds.add(cmd);
            config.setRunCmd(true);
            config.setCmds(cmds);
        }

        if(cliParser.hasOption("mon")){
            config.setMonitor(true);
        }

        if(cliParser.hasOption("memory")){

            config.setMemory(Integer.parseInt(cliParser.getOptionValue("memory")));
        }

        if(cliParser.hasOption("cores")){

            config.setMemory(Integer.parseInt(cliParser.getOptionValue("cores")));
        }

        if(cliParser.hasOption("ipcRetries")){
            config.setIpcRetries(Integer.parseInt(cliParser.getOptionValue("ipcRetries")));
        }

        if(cliParser.hasOption("ua")){

            config.setUa(cliParser.getOptionValue("ua"));
        }

        return config;
    }


    public static Map<String, Long> parseResourcesString(String resourcesStr) {
        Map<String, Long> resources = new HashMap<>();

        // Ignore the grouping "[]"
        if (resourcesStr.startsWith("[")) {
            resourcesStr = resourcesStr.substring(1);
        }
        if (resourcesStr.endsWith("]")) {
            resourcesStr = resourcesStr.substring(0, resourcesStr.length());
        }

        for (String resource : resourcesStr.trim().split(",")) {
            resource = resource.trim();
            if (!resource.matches("^[^=]+=\\d+\\s?\\w*$")) {
                throw new IllegalArgumentException("\"" + resource + "\" is not a " +
                        "valid resource type/amount pair. " +
                        "Please provide key=amount pairs separated by commas.");
            }
            String[] splits = resource.split("=");
            String key = splits[0], value = splits[1];
            String units = ResourceUtils.getUnits(value);
            String valueWithoutUnit = value.substring(
                    0, value.length() - units.length()).trim();
            Long resourceValue = Long.valueOf(valueWithoutUnit);
            if (!units.isEmpty()) {
                resourceValue = UnitsConversionUtil.convert(units, "Mi", resourceValue);
            }
            if (key.equals("memory")) {
                key = ResourceInformation.MEMORY_URI;
            }
            resources.put(key, resourceValue);
        }
        return resources;
    }

    public static GBWYarnCluster getYarnCluster(GBWScanYarnConfig config,CloseableHttpClient httpClient,Host host){

        try {
            HttpGet request = new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),"/ws/v1/cluster/metrics")
                    .addHead("User-Agent",config.getUa())
                    .setTimeout(config.getConTimeout(),config.getReadTimeout())
                    .build();

            GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

            Gson gson = new Gson();
            return gson.fromJson(response.getContent(),GBWYarnCluster.class);
        }catch (Exception e){


        }
        return null;
    }

    public static GBWYarnApp createYarnApp(GBWScanYarnConfig config,CloseableHttpClient httpClient,Host host){

        try {
            HttpPost request = new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),"/ws/v1/cluster/apps/new-application")
                    .addHead("User-Agent",config.getUa())
                    .setTimeout(config.getConTimeout(),config.getReadTimeout())
                    .build();

            GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

            Gson gson = new Gson();

            return gson.fromJson(response.getContent(),GBWYarnApp.class);
        }catch (Exception e){


        }
        return null;
    }

    public static HttpPost createYarnAppSubmitRequest(GBWScanYarnConfig config,CloseableHttpClient httpClient,GBWYarnApp app,Host host) throws IOException {

        HttpPost request = new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),"/ws/v1/cluster/apps")
                .addHead("User-Agent",config.getUa())
                .addHead("Accept"," application/json")
                .addHead("Content-Type"," application/json")
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .postString(createYarnPostJson(app,config),false)
                .build();


        return request;
    }


    public static String createYarnPostJson(GBWYarnApp app,GBWScanYarnConfig config){

        Gson gson = new Gson();

        GBWYarnAppPostData postData = new GBWYarnAppPostData(config,app);

        return gson.toJson(postData);
    }

}
