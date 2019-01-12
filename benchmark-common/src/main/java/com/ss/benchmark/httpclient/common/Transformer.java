package com.ss.benchmark.httpclient.common;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// I wanted this to be a static inner class of BasePerformanceTest, but
// that didn't seem to work when specifying the listener from Maven-land.
// Don't know if the same problem exists if using a suite file.
public class Transformer  implements IAnnotationTransformer {

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Can't easily eliminate the dupe in java:(
        java.util.Optional.ofNullable(System.getProperty("bm.test.executions"))
                .ifPresent(s -> {
                    if (annotation.getInvocationCount() != 1) {
                        annotation.setInvocationCount(Integer.parseInt(s));
                    }
                });

        java.util.Optional.ofNullable(System.getProperty("bm.test.workers"))
                .ifPresent(s -> {
                    if (annotation.getThreadPoolSize() != 1) {
                        annotation.setThreadPoolSize(Integer.parseInt(s));
                    }
                });
    }
}
