package com.gbw.scanner.source;

import com.gbw.scanner.source.elasticsearch.GBWESHostSource;
import com.gbw.scanner.source.elasticsearch.GBWESSourceConfig;
import com.gbw.scanner.source.file.GBWFileLineSource;
import com.gbw.scanner.source.file.GBWFileLineSourceConfig;
import com.gbw.scanner.source.fofa.GBWFoFaSource;
import com.gbw.scanner.source.fofa.GBWFoFaSourceConfig;
import com.gbw.scanner.source.shodan.GBWShodanSource;
import com.gbw.scanner.source.shodan.GBWShodanSourceConfig;
import com.gbw.scanner.utils.GsonUtils;

public class GBWHostSourceFactory {

    private GBWHostSourceFactory(){

    }

    public static GBWHostSource create(GBWHostSourcePoolConfigEntry config) throws Exception {

        String type = config.getType();
        GBWHostSource source = null;

        if(type.equals(GBWHostSource.fileLine)){

            source =  new GBWFileLineSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWFileLineSourceConfig.class));

        }else if(type.equals(GBWHostSource.es)){

            source = new GBWESHostSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWESSourceConfig.class));
        }else if(type.equals(GBWHostSource.fofa)) {

            source = new GBWFoFaSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWFoFaSourceConfig.class));
        }else if(type.equals(GBWHostSource.shodan)){

            source = new GBWShodanSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWShodanSourceConfig.class));
        }

        if(source!=null)
            source.open();

        return source;
    }

    public static GBWHostSource create(String type,String json) throws Exception {

        GBWHostSource source = null;

        if(type.equals(GBWHostSource.fileLine)){

            source =  new GBWFileLineSource(GsonUtils.loadConfigFromJson(json,GBWFileLineSourceConfig.class));

        }else if(type.equals(GBWHostSource.es)){

            source = new GBWESHostSource(GsonUtils.loadConfigFromJson(json, GBWESSourceConfig.class));
        }else if(type.equals(GBWHostSource.fofa)) {

            source = new GBWFoFaSource(GsonUtils.loadConfigFromJson(json, GBWFoFaSourceConfig.class));
        }else if(type.equals(GBWHostSource.shodan)){

            source = new GBWShodanSource(GsonUtils.loadConfigFromJson(json, GBWShodanSourceConfig.class));
        }

        if(source!=null)
            source.open();

        return source;
    }
}
