package com.gbw.scanner.sink.es;



import com.gbw.scanner.geoip.GeoIPConfigItem;

import java.io.IOException;


public final class ESClientFactory {

    private ESClientFactory(){

    }

    public static ESClient create(ESConfigItem configItem, GeoIPConfigItem geoIPConfigItem) throws IOException {

        String type = configItem.getClientType();

        if(type.equalsIgnoreCase("bulk")){

            return new BulkESClient(configItem,geoIPConfigItem);
        }else{

            return new SimpleESClient(configItem,geoIPConfigItem);
        }
    }
}
