# Issue
This is an attempt to compare performance between rxnetty and reactor netty http clients.

# Building the common code

This is the first thing you need to do.

```sh
mvn clean install
``` 

# Running a server the HTTP clients will hit
The `mockapplication` module is a wiremock based application for stubbing http
client benchmark use cases.  The HTTP clients under test will submit requests
to this.

To start the server:

```sh
mvn -pl mockapplication compile exec:java
```

# Running the client load tests

### reactor-netty

```sh
mvn -Pperformance -pl reactornettybenchmark verify
```

### rxnettybenchmark

```sh
mvn -Pperformance -pl rxnettybenchmark verify
```

### apacheasyncbenchmark

```sh
mvn -Pperformance -pl apacheasyncbenchmark verify
```
# Configuration

### Number of test runs

By default, it is configured to 10,000. 

# Issues Observed

See github issues?
