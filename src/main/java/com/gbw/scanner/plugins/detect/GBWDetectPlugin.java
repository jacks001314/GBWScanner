package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.GBWScannerPlugin;
import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.detect.tcp.GBWDetectTCP;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.GsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWDetectPlugin implements GBWScannerPlugin {



    public final static String DETECTTCP = "detectTCP";
    public final static String DETECTUDP = "detectUDP";

    private Map<String,GBWDetect> detectMap;
    private GBWDetectConfig config;
    private GBWDetectRuleSplitQueue splitQueue;
    private SinkQueue sinkQueue;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;



    public GBWDetectPlugin(GBWDetectConfig config,SinkQueue sinkQueue) throws IOException {


        this.detectMap = new HashMap<>();
        this.config = config;
        this.sinkQueue = sinkQueue;

        this.splitQueue = new GBWDetectRuleSplitQueue();

        List<GBWDetectTypeConfig> types = config.getDetects();

        if(types!=null){

            for(GBWDetectTypeConfig typeConfig:types){

                if(typeConfig.isOn()){
                    String type = typeConfig.getName();
                    if(type.equals(DETECTTCP)){
                        detectMap.put(DETECTTCP,new GBWDetectTCP(config,GsonUtils.loadConfigFromJsonFile(typeConfig.getCpath(),GBWDetectRuleConfig.class)));
                    }else if(type.equals(DETECTUDP)){

                    }else{

                    }
                }
            }
        }
    }

    @Override
    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(config.getThreads());

        for(int i = 0; i < config.getThreads(); ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWDetectThread(splitQueue,sinkQueue), 0, 1, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean accept(Host host) {

        List<String> detectTypes = host.getTypes();

        if(detectTypes == null ||detectTypes.isEmpty())
            return false;

        for(String detectType:detectTypes){

            if(detectMap.containsKey(detectType))
                return true;
        }

        return false;
    }

    private void doDetectRuleSplit(GBWDetect detect, Host host){

        GBWDetectRuleConfig ruleConfig = detect.getRuleConfig();
        int n = config.getThreads();
        int dn = ruleConfig.getRulesNumber();
        int dv = dn/n;

        if(dv<=n||dv==dn){
            splitQueue.put(new GBWDetectRuleSplit(detect,host,0,dn-1));
        }else{

            GBWDetectRuleSplit split =null;
            int dd = 0;

            while(dd<dn){

                if(dd+dv>=dn){

                    split.setEnd(dn-1);
                    break;
                }

                split = new GBWDetectRuleSplit(detect,host,dd,dd+dv-1);
                dd+=dv;

                splitQueue.put(split);
            }
        }
    }

    @Override
    public void process(Host host) {

        List<String> detectTypes = host.getTypes();
        if(detectTypes == null||detectTypes.isEmpty())
            return;


        for(String detectType:detectTypes){

            GBWDetect detect = detectMap.get(detectType);

            if(detect!=null){

                doDetectRuleSplit(detect,host);
            }
        }

    }

}
