package com.gbw.scanner.source;

import com.gbw.scanner.source.elasticsearch.GBWESHostSource;
import com.gbw.scanner.source.elasticsearch.GBWESSourceConfig;
import com.gbw.scanner.source.file.GBWFileLineSource;
import com.gbw.scanner.source.file.GBWFileLineSourceConfig;
import com.gbw.scanner.source.fofa.GBWFoFaAPISource;
import com.gbw.scanner.source.fofa.GBWFoFaAPISourceConfig;
import com.gbw.scanner.source.fofa.GBWFoFaSource;
import com.gbw.scanner.source.fofa.GBWFoFaSourceConfig;
import com.gbw.scanner.source.iprange.GBWIPRangeSource;
import com.gbw.scanner.source.iprange.GBWIPRangeSourceConfig;
import com.gbw.scanner.source.shodan.GBWShodanAPISource;
import com.gbw.scanner.source.shodan.GBWShodanAPISourceConfig;
import com.gbw.scanner.source.shodan.GBWShodanSource;
import com.gbw.scanner.source.shodan.GBWShodanSourceConfig;
import com.gbw.scanner.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWHostSourceFactory {

    private static final Logger log = LoggerFactory.getLogger(GBWHostSourcePool.class);

    private GBWHostSourceFactory(){

    }

    public static GBWHostSource create(GBWHostSourcePoolConfigEntry config) throws Exception {

        String type = config.getType();
        GBWHostSource source = null;

        log.info("Create source type:"+type);
        if(type.equals(GBWHostSource.fileLine)){

            source =  new GBWFileLineSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWFileLineSourceConfig.class));

        }else if(type.equals(GBWHostSource.es)){

            source = new GBWESHostSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWESSourceConfig.class),config.getTv());
        }else if(type.equals(GBWHostSource.fofa)) {

            source = new GBWFoFaSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWFoFaSourceConfig.class));
        }else if(type.equals(GBWHostSource.shodan)){

            source = new GBWShodanSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWShodanSourceConfig.class));
        }else if(type.equals(GBWHostSource.ipRange)){

            source = new GBWIPRangeSource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWIPRangeSourceConfig.class),config.getTv());
        } else if(type.equals(GBWHostSource.fofaAPI)){

            source = new GBWFoFaAPISource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWFoFaAPISourceConfig.class));
        } else if(type.equals(GBWHostSource.shodanAPI)){

            source = new GBWShodanAPISource(GsonUtils.loadConfigFromJsonFile(config.getCpath(), GBWShodanAPISourceConfig.class));
        }

        else{

            log.error("Unkown source type:"+type);
        }

        if(source!=null)
            source.open();

        return source;
    }

    public static GBWHostSource create(String type,String json) throws Exception {

        GBWHostSource source = null;
        log.info("Create source type:"+type);
        if(type.equals(GBWHostSource.fileLine)){

            source =  new GBWFileLineSource(GsonUtils.loadConfigFromJson(json,GBWFileLineSourceConfig.class));

        }else if(type.equals(GBWHostSource.es)){

            source = new GBWESHostSource(GsonUtils.loadConfigFromJson(json, GBWESSourceConfig.class),0);
        }else if(type.equals(GBWHostSource.fofa)) {

            source = new GBWFoFaSource(GsonUtils.loadConfigFromJson(json, GBWFoFaSourceConfig.class));
        }else if(type.equals(GBWHostSource.shodan)){

            source = new GBWShodanSource(GsonUtils.loadConfigFromJson(json, GBWShodanSourceConfig.class));
        } else if(type.equals(GBWHostSource.ipRange)){

            source = new GBWIPRangeSource(GsonUtils.loadConfigFromJson(json, GBWIPRangeSourceConfig.class),0);
        } else if(type.equals(GBWHostSource.fofaAPI)){

            source = new GBWFoFaAPISource(GsonUtils.loadConfigFromJson(json, GBWFoFaAPISourceConfig.class));
        } else if(type.equals(GBWHostSource.shodanAPI)){

            source = new GBWShodanAPISource(GsonUtils.loadConfigFromJson(json, GBWShodanAPISourceConfig.class));
        }


        else{
            log.error("Unkown source type:"+type);
        }

        if(source!=null)
            source.open();

        return source;
    }

 }
