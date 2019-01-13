package com.ss.benchmark.httpclient.common;

/**
 * Created by ssrinivasa on 12/13/18.
 */

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Payloads {

    public Payloads() {}

    public static final String HELLO = "Hello!";
    public static final String SHORT = mkStr(550);
    public static final String LONG = mkStr(762_050);

    private static String mkStr(int i) {
        return IntStream.range(0, i).mapToObj(Void -> "a").collect(Collectors.joining());
    }
}

