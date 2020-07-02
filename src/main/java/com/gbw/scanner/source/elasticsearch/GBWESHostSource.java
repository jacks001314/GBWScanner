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

public class GBWESHostSource implements GBWHostSource {

    private GBWESSourceConfig config;
    private Client esClient;
    private AssetsIPS assetsIPS;
    private GBWSourceStatus sourceStatus;

    public GBWESHostSource(GBWESSourceConfig config) throws Exception {

        this.config = config;
        this.sourceStatus = new GBWSourceStatus(config.getStatusFile(),config.getTv());
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

        if(!isTimeout(curTime))
            return 0;

        for (GBWESSearchRule rule : config.getRules()) {

            SearchRequestBuilder searchRequestBuilder = ESUtil.makeESSearch(esClient, assetsIPS, rule, sourceStatus.getLastCheckTime(), curTime);
            SearchResponse searchResponse = searchRequestBuilder.get();

            count += processResponse(sourcePool,searchResponse, rule);

        }

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
        return sourceStatus.isTimeout(curTime);
    }

    @Override
    public void reopen(long curTime) throws Exception {

        sourceStatus.updateStatusTime(curTime);
    }
}
