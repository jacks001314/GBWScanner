package com.gbw.scanner.sink;

import com.gbw.scanner.sink.es.ESIndexable;

import java.util.List;

/**
 * Created by dell on 2018/6/27.
 */
public interface SinkQueue {



    void put(ESIndexable entry);

    void put(List<ESIndexable> arr);

    ESIndexable take();

    boolean isEmpty();

    long getCount();


}
