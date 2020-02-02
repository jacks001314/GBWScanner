package com.gbw.scanner.sink.es;

import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.script.Script;

import java.io.IOException;


public interface ESIndexable {

    XContentBuilder dataToJson(XContentBuilder cb) throws IOException;

    String getIndexMapping();

    String getIndexNamePrefix();

    String getIndexDocType();

    default String getSrcIP(){
        return null;
    }

    default String getDstIP(){

        return null;
    }

   default String getAssetsIP(){

        return null;
   }

    default Script getUpdateScript(){
        return null;
    }

    default boolean indexISConstants(){
        return false;
    }

    default String getId(){

        return TextUtils.getUUID();
    }
}
