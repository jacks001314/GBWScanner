package com.gbw.scanner.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class GsonUtils {


    public static final <T> T loadConfigFromJsonFile(String path,Class<T> type) throws IOException {

        Gson gson = new Gson();

        return gson.fromJson(Files.newBufferedReader(Paths.get(path)),type);
    }


}
