package com.gbw.scanner.elasticsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dell on 2018/7/10.
 */
public class MoreAggItem {

    private List<String> keys;
    private long count;

    public MoreAggItem(){

        this.count = 0;
        this.keys = new ArrayList<>();
    }

    public MoreAggItem(long count, String... keyArgs){

        this.count = count;
        keys = Arrays.asList(keyArgs);

    }


    public void add(String key){

        this.keys.add(key);
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String toString(){

        StringBuffer sb = new StringBuffer();

        keys.forEach(k->sb.append(k+"--------"));
        sb.append(Long.toString(count));

        return sb.toString();
    }

}
