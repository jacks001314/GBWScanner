package com.gbw.scanner.utils;

import com.gbw.scanner.elasticsearch.ESConfig;
import com.gbw.scanner.elasticsearch.ESStringQueryBuilder;
import com.gbw.scanner.elasticsearch.ValueRange;
import com.gbw.scanner.source.GBWESSearchRule;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

public class ESUtil {

    public final static Client getClient(ESConfig esConfig){

        Settings settings = Settings.builder()
                .put("cluster.name",esConfig.getClusterName()).build();

        TransportClient esClient = new PreBuiltTransportClient(settings);

        for(String host:esConfig.getEsHosts()){
            try {
                esClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),esConfig.getEsPort()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        }

        return esClient;
    }


    public static Client getClient(String cfname){

        try {
            ESConfig esConfig = GsonUtils.loadConfigFromJsonFile(cfname,ESConfig.class);
            return getClient(esConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SearchRequestBuilder makeESSearch(Client client,AssetsIPS assetsIPS,GBWESSearchRule searchRule,long startTime,long endTime){


        QueryBuilder queryBuilder = new ESStringQueryBuilder()
                .field(searchRule.getTimeField(), ValueRange.of(startTime,endTime))
                .append(searchRule.getSearch())
                .append(assetsIPS.getSearch(searchRule.getIpField()))
                .build();


        TermsAggregationBuilder aggregationBuilder = terms("ip").field(searchRule.getIpField()).size(Integer.MAX_VALUE);

        if(!TextUtils.isEmpty(searchRule.getHostField())){

            TermsAggregationBuilder host = terms("host").field(searchRule.getHostField()).size(Integer.MAX_VALUE);
            aggregationBuilder.subAggregation(host);
            aggregationBuilder = host;
        }

        aggregationBuilder.subAggregation(terms("port").field(searchRule.getPortField()).size(Integer.MAX_VALUE));


        SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
                .setIndices(searchRule.getIndices())
                .setTypes(searchRule.getType())
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);

        return searchRequestBuilder;
    }


}
