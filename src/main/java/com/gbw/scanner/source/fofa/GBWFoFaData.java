package com.gbw.scanner.source.fofa;

import com.gbw.scanner.utils.GsonUtils;

import java.io.IOException;
import java.util.List;

public class GBWFoFaData {

    private String mode;
    private boolean error;
    private String query;
    private int page;
    private int size;
    private List<List<String>> results;


    public List<List<String>> getResults() {
        return results;
    }

    public void setResults(List<List<String>> results) {
        this.results = results;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static void main(String[] args) throws IOException {

        String path = "F:\\shajf_dev\\antell\\3.4\\GBWScanner\\target\\api.data";
        GBWFoFaData foFaData = GsonUtils.loadConfigFromJsonFile(path,GBWFoFaData.class);
        System.out.println(foFaData);
    }

}
