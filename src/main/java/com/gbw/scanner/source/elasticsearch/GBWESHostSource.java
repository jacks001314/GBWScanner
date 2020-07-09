package com.gbw.scanner.source.elasticsearch;

import com.gbw.scanner.Host;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.gbw.scanner.source.GBWSourceStatus;
import com.gbw.scanner.utils.AssetsIPS;
import com.gbw.scanner.utils.ESUtil;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWESHostSource implements GBWHostSource {

    private static final Logger log = LoggerFactory.getLogger(GBWESHostSource.class);

    private GBWESSourceConfig config;
    private Client esClient;
    private AssetsIPS assetsIPS;
    private GBWSourceStatus sourceStatus;

    public GBWESHostSource(GBWESSourceConfig config,long tv) throws Exception {

        this.config = config;

        if(!TextUtils.isEmpty(config.getStatusFile())){

            this.sourceStatus = new GBWSourceStatus(config.getStatusFile(),tv);
        }else{
            this.sourceStatus = null;
        }
    }


    private int processResponse(GBWHostSourcePool sourcePool,SearchResponse response, GBWESSearchRule rule) {

        int count = 0;
        Terms ipTerms = response.getAggregations().get("ip");
        boolean isHost = !TextUtils.isEmpty(rule.getHostField());

        for (Terms.Bucket ipTerm : ipTerms.getBuckets()) {

            if (isHost) {

                Terms hostTerms = ipTerm.getAggregations().get("host");

                for (Terms.Bucket hostTerm : hostTerms.getBuckets()) {

                    Terms portTerms = hostTerm.getAggregations().get("port");
                    for (Terms.Bucket portTerm : portTerms.getBuckets()) {

                        Host host = new Host(hostTerm.getKeyAsString(), ipTerm.getKeyAsString(), Integer.parseInt(portTerm.getKeyAsString()), rule.getScanType(), rule.getProto());
                        count++;
                        sourcePool.put(host);

                    }
                }
            } else {

                Terms portTerms = ipTerm.getAggregations().get("port");
                for (Terms.Bucket portTerm : portTerms.getBuckets()) {

                    Host host = new Host(ipTerm.getKeyAsString(), ipTerm.getKeyAsString(), Integer.parseInt(portTerm.getKeyAsString()), rule.getScanType(), rule.getProto());
                    count++;
                    sourcePool.put(host);
                }
            }

        }
        return count;
    }

    @Override
    public void open() throws Exception {

        esClient = ESUtil.getClient(config.getEsConfig());

        if(!TextUtils.isEmpty(config.getXmlPath()))
            assetsIPS = new AssetsIPS(config.getXmlPath());
        else
            assetsIPS = null;

    }

    @Override
    public int read(GBWHostSourcePool sourcePool) {

        long curTime = System.currentTimeMillis();
        int count = 0;

        if(!config.isRemoveWhenReadEnd()&&!isTimeout(curTime))
            return 0;

        for (GBWESSearchRule rule : config.getRules()) {

            SearchRequestBuilder searchRequestBuilder = ESUtil.makeESSearch(esClient, assetsIPS, rule,
                    sourceStatus==null?0:sourceStatus.getLastCheckTime(), curTime);

            SearchResponse searchResponse = searchRequestBuilder.get();

            count += processResponse(sourcePool,searchResponse, rule);

        }

        log.info(String.format("Load host to scan queue from es,the number:%d",count));

        if(sourceStatus!=null)
            sourceStatus.updateStatusTime(curTime);
        /*return 0 than can re search*/
        return 0;
    }

    @Override
    public void close() {

        if(config.isRemoveWhenReadEnd())
            esClient.close();
    }

    @Override
    public boolean isRemove() {
        return config.isRemoveWhenReadEnd();
    }

    @Override
    public boolean isTimeout(long curTime) {
        return sourceStatus == null?true:sourceStatus.isTimeout(curTime);
    }

    @Override
    public void reopen(long curTime) throws Exception {

        if(sourceStatus!=null)
            sourceStatus.updateStatusTime(curTime);
    }
}
