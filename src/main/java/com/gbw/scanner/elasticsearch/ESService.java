package com.gbw.scanner.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

public class ESService {

    private final Client client;
    private final String index;
    private final String type;

    public ESService(Client client, String index, String type) {

        this.client = client;
        this.index = index;
        this.type = type;
    }

    private SearchRequestBuilder prepare() {

        return client.prepareSearch()
                .setIndices(index)
                .setTypes(type);
    }

    public long count() {

        try {


            SearchResponse searchResponse = prepare()
                    .setQuery(matchAllQuery())
                    .setSize(0)
                    .get();

            return searchResponse.getHits().totalHits;
        } catch (Exception e) {

            return 0;
        }
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
