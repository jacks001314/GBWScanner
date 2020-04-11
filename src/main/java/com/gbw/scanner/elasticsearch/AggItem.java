package com.gbw.scanner.elasticsearch;

/**
 * Created by dell on 2017/7/20.
 */
public class AggItem {

    private String key;
    private long count;

    public AggItem(String key, long count){

        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String toString(){

        return String.format("%s--------%d",key,count);
    }


}
