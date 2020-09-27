package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.xmap.api.utils.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GBWWeblogicVersion {

    public static String getVersionByT3(GBWScanWeblogicConfig config, Host host) {

        Connection connection = GBWWeblogicConnection.makeConnection(config, host);

        if (connection == null)
            return "";
        try {
            String proto = host.getProto();

            if (TextUtils.isEmpty(proto) || proto.equals("http"))
                proto = "t3";


            else if (proto.equals("https"))
                proto = "t3s";

            if (!proto.startsWith("t3"))
                proto = "t3";

            String header = String.format("%s 7.0.0.0\nAS:10\nHL:19\n\n", proto);

            connection.send(header.getBytes());
            String version = connection.readLine();

            //  System.out.println(version);
            if (TextUtils.isEmpty(version) || !version.contains("HELO:"))
                return "";

            return version.replace("HELO:", "").replace(".false", "").replace(".true", "");

        } catch (Exception e) {
            return "";
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

    }

    /**
     * 通过 HTTP 获取 weblogic 版本
     */
    public static String getVersionByHttp(GBWScanWeblogicConfig config, Host host) {

        String version = "";
        String proto = host.getProto();

        if (TextUtils.isEmpty(proto) || proto.equals("t3"))
            proto = "http";

        if (proto.equals("t3s"))
            proto = "https";

        if (!proto.startsWith("http"))
            proto = "http";

        String url = String.format("%s://%s:%d/console/login/LoginForm.jsp", proto, host.getIp(), host.getPort());

        try {
            Document doc = Jsoup.connect(url).timeout(config.getReadTimeout()).get();
            String versionTmpStr = doc.getElementById("footerVersion").text();

            if (!TextUtils.isEmpty(versionTmpStr)) {
                Pattern pattern = Pattern.compile("[\\d\\.]+", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(versionTmpStr);
                if(matcher.find())
                    version = matcher.group(0);
            }
        } catch (Exception e) {
            version = "";
        }
        return version;
    }


    public static String getVersion(GBWScanWeblogicConfig config, Host host) {

        String version = getVersionByT3(config,host);

        if(TextUtils.isEmpty(version)){

            version = getVersionByHttp(config,host);
        }

        return version;
    }

}
