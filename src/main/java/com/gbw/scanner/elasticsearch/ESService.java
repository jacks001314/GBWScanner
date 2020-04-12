package com.gbw.scanner.elasticsearch;

import com.gbw.scanner.utils.ESUtil;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
public class ESService {

    private final Client client;
    private final String index;
    private final String type;

    public ESService(Client client, String index, String type) {

        this.client = client;
        this.index = index;
        this.type = type;
    }

    public SearchRequestBuilder prepare() {

        return client.prepareSearch()
                .setIndices(index)
                .setTypes(type);
    }


    public SearchRequestBuilder prepare(String q){
        return client.prepareSearch()
                .setIndices(index)
                .setTypes(type)
                .setQuery(ESUtil.makeQuery(q));
    }

    public long count(String queryString) {

        try {

            SearchResponse searchResponse = prepare()
                    .setQuery(ESUtil.makeQuery(queryString))
                    .setSize(0)
                    .get();

            return searchResponse.getHits().totalHits;
        } catch (Exception e) {

            return 0;
        }

    }


    public long maxValue(String q, String field) {

        SearchResponse searchResponse = prepare(q)
                .addAggregation(max("m").field(field))
                .get();
        Max max = searchResponse.getAggregations().get("m");
        return (long)max.getValue();
    }

    public long minValue(String q, String field) {

        SearchResponse searchResponse = prepare(q)
                .addAggregation(min("m").field(field))
                .get();
        Min min = searchResponse.getAggregations().get("m");
        return (long)min.getValue();
    }


    public long sumValue(String q, String field) {


        SearchResponse searchResponse = prepare(q)
                .addAggregation(sum("sum").field(field))
                .get();

        Sum sum = searchResponse.getAggregations().get("sum");

        return (long)sum.getValue();
    }

    private  List<Terms.Bucket> getTermsBucket(Aggregations agg, String name){

        return (List<Terms.Bucket>) ((Terms)agg.get(name)).getBuckets();
    }

    public List<AggItem> topBYTerm(String q, String field, int topN, boolean isasc) {

        SearchResponse searchResponse = prepare(q)
                .addAggregation(terms("term").field(field).size(topN).order(Terms.Order.count(isasc)))
                .get();

        return getTermsBucket(searchResponse.getAggregations(),"term").stream().map(bucket -> {
            return new AggItem(bucket.getKeyAsString(), bucket.getDocCount());
        }).collect(Collectors.toList());

    }

    public List<MoreAggItem> topBYTerms2(String q, int topN, boolean isasc, String f1, String f2) {

        SearchResponse searchResponse =   prepare(q)
                .addAggregation(terms("term1").field(f1).size(topN).order(Terms.Order.count(isasc))
                        .subAggregation(terms("term2").field(f2).size(topN).order(Terms.Order.count(isasc))))
                .get();

        List<MoreAggItem> moreAggItems = new ArrayList<>();

        Terms term1 = searchResponse.getAggregations().get("term1");

        for (Terms.Bucket bucket : term1.getBuckets()) {

            Terms term2 = bucket.getAggregations().get("term2");
            for (Terms.Bucket item : term2.getBuckets()) {

                MoreAggItem moreAggItem = new MoreAggItem(item.getDocCount(), bucket.getKeyAsString(), item.getKeyAsString());

                moreAggItems.add(moreAggItem);
            }
        }

        return moreAggItems;
    }

    public SearchHit[] search(String q,int page,int pageSize,String sortField,boolean isDec){

        SearchRequestBuilder sq = prepare(q)
                .setFrom(page)
                .setSize(pageSize);

        if(!TextUtils.isEmpty(sortField))
        {
            sq.addSort(sortField,isDec? SortOrder.DESC:SortOrder.ASC);
        }

        SearchResponse response = sq.get();

        return response.getHits().getHits();
    }

    public long delete(String q){


        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(ESUtil.makeQuery(q))
                .source(index)
                .get();

        return response.getDeleted();
    }

    public Client getClient() {
        return client;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }


}
