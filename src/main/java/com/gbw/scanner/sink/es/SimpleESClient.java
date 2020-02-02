package com.gbw.scanner.sink.es;

import com.gbw.scanner.geoip.GeoIPConfigItem;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class SimpleESClient extends AbstractESClient{

    Logger logger = LoggerFactory.getLogger(SimpleESClient.class);

    public SimpleESClient(ESConfigItem configItem, GeoIPConfigItem geoIPConfigItem) throws IOException {
        super(configItem,geoIPConfigItem);
    }

    @Override
    public void index(ESIndexable indexable) {

        IndexRequest indexRequest = null;
        try {

            if(indexable.getUpdateScript()==null) {
                indexRequest = makeIndexRequest(indexable);
                IndexResponse indexResponse = esClient.index(indexRequest).actionGet();
            }else{
                update(indexable);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void update(ESIndexable indexable) throws IOException {

        UpdateRequest updateRequest = makeUpdateRequest(indexable);

        esClient.update(updateRequest, new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {

                logger.info("update es is ok--------------------------");
            }

            @Override
            public void onFailure(Exception e) {

                logger.info("update es failed --------------------------");
            }
        });
    }


}
