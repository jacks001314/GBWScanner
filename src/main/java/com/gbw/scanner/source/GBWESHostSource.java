package com.gbw.scanner.source;

import com.gbw.scanner.Host;
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

public class GBWESHostSource extends GBWAbstractHostSource {

    private GBWESSourceConfig config;
    private Client esClient;
    private long lastCheckTime;
    private String statusFPath;
    private AssetsIPS assetsIPS;

    public GBWESHostSource(GBWESSourceConfig config) throws Exception {

        super(config);
        this.config = config;
        this.esClient = ESUtil.getClient(config.getEsConfig());
        this.assetsIPS = new AssetsIPS(config.getXmlPath());
        this.initStatusFile(config.getStatusFile());
    }

    private void initStatusFile(String statusFileName) throws IOException {
        this.statusFPath = statusFileName;

        Path path = Paths.get(this.statusFPath);
        if (!Files.exists(path)){
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            this.lastCheckTime = 0L;
        } else {
            List<String> lines = Files.readAllLines(path);
            if (lines != null && lines.size() != 0) {
                this.lastCheckTime = Long.parseLong((String)lines.get(0));
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

    public void preRead() {
    }

    public int read() {

        long curTime = System.currentTimeMillis();
        int count = 0;

        for(GBWESSearchRule rule:config.getRules()){

            SearchRequestBuilder searchRequestBuilder = ESUtil.makeESSearch(esClient,assetsIPS,rule,lastCheckTime,curTime);
            SearchResponse searchResponse = searchRequestBuilder.get();

            count += processResponse(searchResponse,rule);

        }

        updateCheckTime(curTime);
        return count;
    }

    public void readEnd() {
        this.esClient.close();
    }

    private int processResponse(SearchResponse response, GBWESSearchRule rule) {

        int count = 0;
        Terms ipTerms = response.getAggregations().get("ip");
        boolean isHost = !TextUtils.isEmpty(rule.getHostField());

        for(Terms.Bucket ipTerm:ipTerms.getBuckets()){

            if(isHost){

                Terms hostTerms = ipTerm.getAggregations().get("host");

                for(Terms.Bucket hostTerm:hostTerms.getBuckets()){

                    Terms portTerms = hostTerm.getAggregations().get("port");
                    for(Terms.Bucket portTerm:portTerms.getBuckets()){

                        Host host = new Host(hostTerm.getKeyAsString(),ipTerm.getKeyAsString(),Integer.parseInt(portTerm.getKeyAsString()),rule.getScanType(),rule.getProto());
                        count++;
                        put(host);

                    }
                }
            }else {

                Terms portTerms = ipTerm.getAggregations().get("port");
                for(Terms.Bucket portTerm:portTerms.getBuckets()){

                    Host host = new Host(ipTerm.getKeyAsString(),ipTerm.getKeyAsString(),Integer.parseInt(portTerm.getKeyAsString()),rule.getScanType(),rule.getProto());
                    count++;
                    put(host);
                }
            }

        }
        return count;
    }

}
