package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.GBWScannerPlugin;
import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.hadoop.spark.GBWScanSparkScript;
import com.gbw.scanner.plugins.scripts.hadoop.yarn.GBWScanYarnScript;
import com.gbw.scanner.plugins.scripts.redis.GBWScanRedisScript;
import com.gbw.scanner.plugins.scripts.web.flink.GBWScanFlinkScript;
import com.gbw.scanner.plugins.scripts.web.solr.dataimport.GBWScanSolrDataImportScript;
import com.gbw.scanner.plugins.scripts.web.solr.velocity.GBWScanSolrVelocityScript;
import com.gbw.scanner.plugins.scripts.web.tomcat.GBWScanAJPScript;
import com.gbw.scanner.plugins.scripts.weblogic.CVE20202555.GBWScanCVE20202555Script;
import com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep.GBWScanBluekeepScript;
import com.gbw.scanner.plugins.scripts.windows.smb.MS17010.GBWScanSMBMS17010Script;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.GsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWScanScriptPlugin implements GBWScannerPlugin {

    public static final String solrVelScan = "scanScriptSolrVelocity";
    public static final String solrDataImportScan = "scanScriptSolrDataImport";
    public static final String ms17010Scan = "scanScriptMS17010";
    public static final String bluekeepScan = "scanScriptBluekeep";
    public static final String flinkScan = "scanScriptFlink";
    public static final String tomcatAJPScan = "scanScriptTomcatAJP";
    public static final String hadoopYarnScan = "scanScriptHadoopYarn";
    public static final String redisScan = "scanScriptRedis";
    public static final String sparkScan = "scanScriptSpark";
    public static final String weblogicCVE20202555 = "weblogicCVE20202555";

    private final GBWScanScriptConfig scanScriptConfig;
    private final GBWScanScriptQueue scanScriptQueue;
    private final Map<String, GBWScanScript> scanScriptMap;
    private final SinkQueue sinkQueue;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public GBWScanScriptPlugin(SinkQueue sinkQueue,GBWScanScriptConfigItem scanScriptConfigItem) throws IOException {

        this.scanScriptQueue = new GBWScanScriptQueue();
        this.scanScriptMap = new HashMap<>();
        this.sinkQueue = sinkQueue;

        this.scanScriptConfig = GsonUtils.loadConfigFromJsonFile(scanScriptConfigItem.getCpath(),GBWScanScriptConfig.class);

        if(scanScriptConfig.getSolrVelocityScriptConfig().isOn()){

            scanScriptMap.put(solrVelScan,new GBWScanSolrVelocityScript(scanScriptConfig.getSolrVelocityScriptConfig()));
        }
        if(scanScriptConfig.getSolrDataImportScriptConfig().isOn()){

            scanScriptMap.put(solrDataImportScan,new GBWScanSolrDataImportScript(scanScriptConfig.getSolrDataImportScriptConfig()));
        }
        if(scanScriptConfig.getFlinkScriptConfig().isOn()){
            scanScriptMap.put(flinkScan,new GBWScanFlinkScript(scanScriptConfig.getFlinkScriptConfig()));
        }

        if(scanScriptConfig.getTomcatAjpScriptConfig().isOn()){

            scanScriptMap.put(tomcatAJPScan,new GBWScanAJPScript(scanScriptConfig.getTomcatAjpScriptConfig()));
        }

        if(scanScriptConfig.getMs17010ScriptConfig().isOn()){

            scanScriptMap.put(ms17010Scan,new GBWScanSMBMS17010Script(scanScriptConfig.getMs17010ScriptConfig()));
        }
        if(scanScriptConfig.getBluekeepScriptConfig().isOn()){
            scanScriptMap.put(bluekeepScan,new GBWScanBluekeepScript(scanScriptConfig.getBluekeepScriptConfig()));
        }
        if(scanScriptConfig.getScanYarnConfig().isOn()){

            scanScriptMap.put(hadoopYarnScan,new GBWScanYarnScript(scanScriptConfig.getScanYarnConfig()));
        }

        if(scanScriptConfig.getScanRedisConfig().isOn()){

            scanScriptMap.put(redisScan,new GBWScanRedisScript(scanScriptConfig.getScanRedisConfig()));
        }
        if(scanScriptConfig.getScanSparkConfig().isOn()){

            scanScriptMap.put(sparkScan,new GBWScanSparkScript(scanScriptConfig.getScanSparkConfig()));
        }

        if(scanScriptConfig.getWeblogicCVE20202555Config().isOn()){
            scanScriptMap.put(weblogicCVE20202555,new GBWScanCVE20202555Script(scanScriptConfig.getWeblogicCVE20202555Config()));
        }
    }

    @Override
    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(scanScriptConfig.getThreads());

        for(int i = 0; i < scanScriptConfig.getThreads(); ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWScanScriptThread(sinkQueue,scanScriptQueue,scanScriptMap), 0, 1, TimeUnit.MINUTES);
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

        List<String> scanTypes = host.getTypes();
        if(scanTypes == null||scanTypes.isEmpty())
            return false;

        for(String type:scanTypes){

            if(scanScriptMap.containsKey(type))
                return true;
        }

        return false;
    }

    @Override
    public void process(Host host) {

        scanScriptQueue.put(host);
    }

}
