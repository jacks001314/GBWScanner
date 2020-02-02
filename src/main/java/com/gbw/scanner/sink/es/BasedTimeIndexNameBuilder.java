package com.gbw.scanner.sink.es;

import java.text.SimpleDateFormat;
import java.util.Date;


public class BasedTimeIndexNameBuilder implements IndexNameBuilder {

    private final static SimpleDateFormat dft = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    public String build(String indexPrefix, String docType) {
        return  new StringBuilder().append(indexPrefix)
                .append("_")
                .append(dft.format(new Date()))
                .toString();
    }

}
