package com.gbw.scanner.source.elasticsearch;

import com.gbw.scanner.Host;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.gbw.scanner.utils.AssetsIPS;
import com.gbw.scanner.utils.ESUtil;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GBWESHostSource implements GBWHostSource {

    private GBWESSourceConfig config;
    private Client esClient;
    private long lastCheckTime;
    private String statusFPath;
    private AssetsIPS assetsIPS;

    public GBWESHostSource(GBWESSourceConfig config) throws Exception {
        this.config = config;
    }

    private void initStatusFile(String statusFileName) throws IOException {
        this.statusFPath = statusFileName;

        Path path = Paths.get(this.statusFPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            this.lastCheckTime = 0L;
        } else {
            List<String> lines = Files.readAllLines(path);
            if (lines != null && lines.size() != 0) {
                this.lastCheckTime = Long.parseLong((String) lines.get(0));
            } else {
                this.lastCheckTime = 0L;
            }
        }

    }

    public void updateCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.statusFPath));
            writer.write(Long.toString(lastCheckTime));
            writer.flush();
            writer.close();
        } catch (IOException var4) {
            var4.printStackTrace();
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

        if(!config.isRemoveWhenReadEnd()&&!TextUtils.isEmpty(config.getStatusFile()))
            initStatusFile(config.getStatusFile());

    }

    @Override
    public int read(GBWHostSourcePool sourcePool) {

        long curTime = System.currentTimeMillis();
        int count = 0;

        for (GBWESSearchRule rule : config.getRules()) {

            SearchRequestBuilder searchRequestBuilder = ESUtil.makeESSearch(esClient, assetsIPS, rule, lastCheckTime, curTime);
            SearchResponse searchResponse = searchRequestBuilder.get();

            count += processResponse(sourcePool,searchResponse, rule);

        }

        if(!config.isRemoveWhenReadEnd())
            updateCheckTime(curTime);

        return count;
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
}
