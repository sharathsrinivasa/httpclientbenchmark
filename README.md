# Issue
This is an attempt to compare performance between rxnetty and reactor netty http clients.

# mockapplication
mockapplication is a wiremock based application for stubbing http client benchmark use cases.

# How to build 
`mvn clean install` 

## IDE
Run MockService 
You could use mvn exec:java -pl mockapplication too.

# Number of test runs
By default, it is configured to 10000. 

# reactornettybenchmark
Run test in ReactorNettyHCPerformanceTests

# rxnettybenchmark
Run test in RxNettyPerformanceTests

## Issues Observed
