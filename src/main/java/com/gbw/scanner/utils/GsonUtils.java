package com.gbw.scanner.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GsonUtils {


    public static final <T> T loadConfigFromJsonFile(String path,Class<T> type) throws IOException {

        Gson gson = new Gson();

        return gson.fromJson(Files.newBufferedReader(Paths.get(path)),type);
    }

    public static final <T> T loadConfigFromJson(String json,Class<T> type) throws IOException {

        Gson gson = new Gson();

        return gson.fromJson(json,type);
    }

    public static String toJson(Object object,boolean isEncode){

        Gson gson = new Gson();
        String res = gson.toJson(object);

        return isEncode?Base64Utils.encode(res):res;
    }



}
