package com.ss.benchmark.httpclient.common;

/**
 * Created by ssrinivasa on 12/13/18.
 */

import com.codahale.metrics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MetricsHelper {

    public static final Logger logger  = LoggerFactory.getLogger(MetricsHelper.class);
    public MetricRegistry metricRegistry = new MetricRegistry();
    protected ScheduledReporter reporter;

    public void initializeMetrics() {
        reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).build();
        reporter.start(1, TimeUnit.HOURS);
    }

    public Meter getRateMeter(String name, String method){
        return getArbitraryMeter(name, method, "rate");
    }

    public Meter getErrorMeter(String name, String method){
        return metricRegistry.meter(MetricRegistry.name(name, method, "errorRate"));
    }

    public Meter getArbitraryMeter(String name, String method, String type){
        return metricRegistry.meter(MetricRegistry.name(name, method, type));
    }

    public Timer getTimingTimer(String name, String method){
        return metricRegistry.timer(MetricRegistry.name(name, method, "timing"));
    }

    public Counter getArbitraryCounter(String name, String method, String type){
        return metricRegistry.counter(MetricRegistry.name(name, method, type));
    }

    public Counter getErrorCounter(String name, String method){
        return getArbitraryCounter(name, method, "errors");
    }

    public Timer getTimingTimer(Class clazz, String method){
        return metricRegistry.timer(MetricRegistry.name(clazz, method, "timing"));
    }

    public Counter getErrorCounter(Class clazz, String method){
        return metricRegistry.counter(MetricRegistry.name(clazz, method, "errors"));
    }

    public void registerArbitraryGauge(String name, String method, String type, Metric T){
        metricRegistry.register(MetricRegistry.name(name, method, type), T);
    }

    public void dumpMetrics(){
        if (reporter != null) {
            logger.info("**************Starting to dump metrics to the reporter");
            reporter.report();
            logger.info("**************Finished dumping metrics to the reporter");
        }
    }

    public void closeMetrics(){
        if (reporter != null) {
            reporter.close();
        }
    }
}
