package com.ss.benchmark.httpclient.common;

/**
 * Created by ssrinivasa on 12/13/18.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class Payloads {

    private static String load(String target) {
        String content = "";
        URL location = Payloads.class.getResource(target);
        if (location == null) {
            throw new IllegalStateException("Unable to locate " + target);
        }
        InputStream loader = Payloads.class.getResourceAsStream(target);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(loader))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }catch (Exception e){
            throw new IllegalStateException(e);
        }
    }

    public static final String LONG_JSON = load("/long.json");

    public static final String SHORT_JSON = load("/short.json");

}

