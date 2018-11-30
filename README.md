# Issue
This is an attempt to reproduce the issue mentioned [here](https://github.com/reactor/reactor-netty/issues/413) in reactor netty. 

Reproducibility : If you are using testng's threadpool and performing HTTPClient call, I could see Connection prematurely closed BEFORE response.

# mockapplication
mockapplication is a wiremock based application for stubbing http client benchmark use cases.

# How to build mockapplication
`mvn clean package` 

## IDE
Run MockService

# reactornettybenchmark
Run test in ReactorNettyHCPerformanceTests

## Exception Observed:

```
reactor.core.Exceptions$ReactiveException: reactor.netty.http.client.HttpClientOperations$PrematureCloseException: Connection prematurely closed BEFORE response
	at reactor.core.Exceptions.propagate(Exceptions.java:326)
	at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:91)
	at reactor.core.publisher.Mono.block(Mono.java:1475)
	at com.ss.test.httpclient.reactornetty.ReactorNettyHCPerformanceTests.executeSync(ReactorNettyHCPerformanceTests.java:123)
	at com.ss.test.httpclient.reactornetty.ReactorNettyHCPerformanceTests.warmupBlocking(ReactorNettyHCPerformanceTests.java:82)
	at sun.reflect.GeneratedMethodAccessor2.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
	at org.testng.internal.MethodInvocationHelper.invokeMethod(MethodInvocationHelper.java:124)
	at org.testng.internal.Invoker.invokeMethod(Invoker.java:583)
	at org.testng.internal.Invoker.invokeTestMethod(Invoker.java:719)
	at org.testng.internal.Invoker.invokeTestMethods(Invoker.java:989)
	at org.testng.internal.TestMethodWorker.invokeTestMethods(TestMethodWorker.java:125)
	at org.testng.internal.TestMethodWorker.run(TestMethodWorker.java:109)
	at org.testng.internal.thread.ThreadUtil$1.call(ThreadUtil.java:52)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
	Suppressed: java.lang.Exception: #block terminated with an error
		at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:93)
		... 17 more
Caused by: reactor.netty.http.client.HttpClientOperations$PrematureCloseException: Connection prematurely closed BEFORE response
reactor.core.Exceptions$ReactiveException: reactor.netty.http.client.HttpClientOperations$PrematureCloseException: Connection prematurely closed BEFORE response
	at reactor.core.Exceptions.propagate(Exceptions.java:326)
	at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:91)
	at reactor.core.publisher.Mono.block(Mono.java:1475)
	at com.ss.test.httpclient.reactornetty.ReactorNettyHCPerformanceTests.executeSync(ReactorNettyHCPerformanceTests.java:123)
	at com.ss.test.httpclient.reactornetty.ReactorNettyHCPerformanceTests.warmupBlocking(ReactorNettyHCPerformanceTests.java:82)
	at sun.reflect.GeneratedMethodAccessor2.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
	at org.testng.internal.MethodInvocationHelper.invokeMethod(MethodInvocationHelper.java:124)
	at org.testng.internal.Invoker.invokeMethod(Invoker.java:583)
	at org.testng.internal.Invoker.invokeTestMethod(Invoker.java:719)
	at org.testng.internal.Invoker.invokeTestMethods(Invoker.java:989)
	at org.testng.internal.TestMethodWorker.invokeTestMethods(TestMethodWorker.java:125)
	at org.testng.internal.TestMethodWorker.run(TestMethodWorker.java:109)
	at org.testng.internal.thread.ThreadUtil$1.call(ThreadUtil.java:52)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
	Suppressed: java.lang.Exception: #block terminated with an error
		at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:93)
		... 17 more
Caused by: reactor.netty.http.client.HttpClientOperations$PrematureCloseException: Connection prematurely closed BEFORE response
```
