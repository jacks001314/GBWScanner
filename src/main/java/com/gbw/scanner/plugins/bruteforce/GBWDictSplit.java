package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.Host;

public class GBWDictSplit {

    private final Host host;
    private final GBWBruteForce bruteForce;
    private final GBWDict dict;

    private int start;
    private int end;
    private int pos;

    public GBWDictSplit(GBWBruteForce bruteForce,Host host,int start,int end){

        this.host = host;
        this.bruteForce = bruteForce;
        this.dict = bruteForce.getDict();
        this.start = start;
        this.end = end;

        this.pos = start;
    }

    public String toString(){

        return String.format("start:%d,end:%d,pos:%d",start,end,pos);
    }

    public Host getHost() {
        return host;
    }

    public boolean hasNext(){

        return pos<=end;
    }

    public GBWDictEntry next(){

        if(pos>end)
            return null;

        GBWDictEntry entry =dict.get(pos);
        pos+=1;
        return entry;
    }

    public GBWBruteForce getBruteForce() {
        return bruteForce;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
