package com.gbw.scanner.sink.es;



public interface ESClient {


    void index(ESIndexable indexable);

    void close();
}
