package com.gbw.scanner.elasticsearch;

import com.gbw.scanner.utils.ESUtil;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.client.Client;

public class ESIndexService {

    private final Client client;

    public ESIndexService(Client client){

        this.client = client;
    }

    public ESIndexService(String cluster,String host,int port) throws Exception{

        this(ESUtil.getClient(cluster,host,port));
    }

    public String[] getIndices(){

        GetIndexResponse response = client.admin().indices().prepareGetIndex().get();
        return response.getIndices();
    }

    public void closeIndex(String index){
        client.admin().indices().prepareClose(index).execute().actionGet();
    }

    public void openIndex(String index){
        client.admin().indices().prepareOpen(index).execute().actionGet();
    }


}
