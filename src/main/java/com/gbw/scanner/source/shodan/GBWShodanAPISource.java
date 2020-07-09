package com.gbw.scanner.source.shodan;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.host.HostReport;
import com.gbw.scanner.Host;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.gbw.scanner.utils.Base64Utils;
import io.reactivex.observers.DisposableObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class GBWShodanAPISource implements GBWHostSource {

    private static final Logger log = LoggerFactory.getLogger(GBWShodanAPISource.class);

    private GBWShodanAPISourceConfig config;
    private ShodanRestApi api;
    private int page;
    private String search;

    public GBWShodanAPISource(GBWShodanAPISourceConfig config){

        this.config = config;
        this.page = 0;

        if(config.isBase64())
            this.search = Base64Utils.decode(config.getSearch());
        else
            this.search = config.getSearch();


    }

    @Override
    public void open() throws Exception {

        api = new ShodanRestApi(config.getKey());

    }

    private String getProto(boolean isSSL){

        String proto = config.getProto();

        if(isSSL){
            if(proto.equals("http"))
                proto = "https";
            if(proto.equals("tcp"))
                proto = "tcps";
        }

        return proto;
    }

    @Override
    public int read(GBWHostSourcePool sourcePool) {

        AtomicInteger count = new AtomicInteger(0);
        api.hostSearch(page, search, null)
                    .subscribe(
                            new DisposableObserver<HostReport>() {
                                @Override
                                public void onNext(HostReport hostReport) {

                                    hostReport.getBanners().forEach(e -> {
                                        Host host = new Host(e.getIpStr(),e.getIpStr(),e.getPort(),config.getScanType(),getProto(e.isSslEnabled()));
                                        //System.out.println(host.getHost()+":"+host.getPort());
                                        sourcePool.put(host);
                                        count.getAndIncrement();
                                    });

                                }

                                @Override
                                public void onError(Throwable throwable) {

                                    log.error(String.format("Read Shodan failed,search:%s,error:%s,page:%d",search,throwable.getMessage(),page));
                                    count.set(0);
                                }

                                @Override
                                public void onComplete() {

                                    //isBreak[0] = true;
                                }
                            }
                    );

        log.info(String.format("Read Shodan search:%s,page:%d,count:%d",search,page,count.get()));
        page++;

        return count.get();
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isRemove() {
        return config.isRemoveWhenReadEnd();
    }

    @Override
    public boolean isTimeout(long curTime) {
        return false;
    }

    @Override
    public void reopen(long curTime) throws Exception {

    }


}
