package com.gbw.scanner.utils;

/**
 * Created by dell on 2017/7/18.
 */
public class ValueRange {

    private long from;
    private long to;

    public ValueRange(long from, long to){

        this.from = from;
        this.to = to;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public static ValueRange of(long from,long to){

        if(from <=0&&to<=0)
            return null;

        return new ValueRange(from,to);
    }

    public static ValueRange of(long lastDay){

        long to = System.currentTimeMillis();
        long tv = lastDay*24*3600*1000;

        long from = to-tv;

        return new ValueRange(from,to);
    }
}
