package com.gbw.scanner.source.iprange;

import com.gbw.scanner.Host;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.gbw.scanner.source.GBWSourceStatus;
import com.gbw.scanner.utils.AssetsIPS;
import com.xmap.api.XMapIPIterator;
import com.xmap.api.utils.DateUtils;
import com.xmap.api.utils.IPUtils;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GBWIPRangeSource implements GBWHostSource {

    private static final Logger log = LoggerFactory.getLogger(GBWIPRangeSource.class);

    private XMapIPIterator ipIterator;
    private GBWIPRangeSourceConfig config;
    private GBWSourceStatus sourceStatus;

    public GBWIPRangeSource(GBWIPRangeSourceConfig config,long tv) throws Exception {

        this.config = config;
        this.ipIterator = new XMapIPIterator();

        if(!TextUtils.isEmpty(config.getStatusFName()))
            this.sourceStatus  = new GBWSourceStatus(config.getStatusFName(),tv);
    }

    @Override
    public void open() throws Exception {

        log.info("OPen IPRange to read-----------------------------");
        if(config.isAssetsFile()){

            AssetsIPS ips = new AssetsIPS(config.getAssetsFile());
            ips.writeFile(config.getWlistPath());
        }

        ipIterator.prepareIterate(config.getWlistPath(),config.getBlistPath());
    }

    private int processConfig(GBWHostSourcePool sourcePool,GBWIPRangePort2ScanType port2ScanType,String ip){


        int count = 0;

        List<Integer> ports = port2ScanType.getFinalPorts();

        for(int port:ports){

            Host host = new Host(ip,ip,port,port2ScanType.getScanTypes(),port2ScanType.getProto());

            count++;

            sourcePool.put(host);
        }

        log.info("Load host number:"+count);
        return count;
    }


    @Override
    public int read(GBWHostSourcePool sourcePool) {

        int count = 0;
        long curIP = ipIterator.getNextIP();
        if(curIP  ==0)
            return 0;

        String ip = IPUtils.ipv4Str(curIP);

        for(GBWIPRangePort2ScanType port2ScanType:config.getPort2ScanTypes()){

            count+= processConfig(sourcePool,port2ScanType,ip);
        }


        return count;
    }

    @Override
    public void close() {

        ipIterator.closeIterate();

    }

    @Override
    public boolean isRemove() {
        return config.isRemoveWhenReadEnd();
    }

    @Override
    public boolean isTimeout(long curTime) {
        return sourceStatus.isTimeout(curTime);
    }

    @Override
    public void reopen(long curTime) throws Exception {

        log.info(String.format("Reopen iprange to read,time:%s", DateUtils.format(curTime)));
        close();
        open();
        sourceStatus.updateStatusTime(curTime);
    }

}
