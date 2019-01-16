package com.ss.benchmark.httpclient.common;

import java.util.concurrent.Callable;

public class Exceptions {

    private Exceptions() {}

    /**
     * Execute the callable returning its value.  If the call throws a checked exception,
     * it is wrapped as a RuntimeException.
     */
    public static <T> T rethrowChecked(Callable<T> c) {
        try {
            return c.call();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
            throw new RuntimeException("Repackaged checked exception as unchecked.  See cause.", e);
        }
    }

}
