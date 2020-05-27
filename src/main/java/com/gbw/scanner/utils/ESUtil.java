package com.gbw.scanner.utils;

import com.gbw.scanner.elasticsearch.ESConfig;
import com.gbw.scanner.elasticsearch.ESStringQueryBuilder;
import com.gbw.scanner.source.elasticsearch.GBWESSearchRule;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

public class ESUtil {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

    public final static Client getClient(ESConfig esConfig){

        Settings settings = Settings.builder()
                .put("cluster.name",esConfig.getClusterName()).build();

        TransportClient esClient = new PreBuiltTransportClient(settings);

        for(String host:esConfig.getEsHosts()){
            try {
                esClient.addTransportAddress(new TransportAddress(InetAddress.getByName(host),esConfig.getEsPort()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        }

        return esClient;
    }

    public final static  QueryBuilder makeQuery(String queryString) {

        if(TextUtils.isEmpty(queryString))
            return new MatchAllQueryBuilder();

        return QueryBuilders.queryStringQuery(queryString);
    }

    public final static Client getClient(String cluster,String host,int port) throws UnknownHostException {

        Settings settings = Settings.builder()
                .put("cluster.name",cluster).build();

        TransportClient esClient = new PreBuiltTransportClient(settings);

        esClient.addTransportAddress(new TransportAddress(InetAddress.getByName(host),port));

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


        ESStringQueryBuilder queryBuilder = new ESStringQueryBuilder()
                .field(searchRule.getTimeField(), ValueRange.of(startTime,endTime))
                .append(searchRule.getSearch());

        if(assetsIPS!=null)
            queryBuilder.append(assetsIPS.getSearch(searchRule.getIpField()));


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
                .setQuery(queryBuilder.build())
                .addAggregation(aggregationBuilder);

        return searchRequestBuilder;
    }

    public static long getIndexTime(String index){

        String dateStr = index.substring(index.lastIndexOf("_")+1);
        {
            if (TextUtils.isEmpty(dateStr)) {
                return 0L;
            } else {
                try {
                    Date date = formatter.parse(dateStr);
                    return date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0L;
                }
            }
        }
    }

    public static boolean dateInRange(ValueRange range,String index){

        long t = getIndexTime(index);

        return t>=range.getFrom()&&t<=range.getTo();
    }


}
