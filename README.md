# Description
This is an attempt to compare performance between different http clients. Currently, we are benchmarking rxnetty, reactor netty, Apache Sync N Async and AsyncHttpClient clients.

# Building

Build everything other than the docker application.

```sh
mvn clean install
```

Build the docker application, which contains all the utilities you need (e.g.
client tests, server, report utilities) to gather benchmarks.

```sh
mvn -pl docker-app package docker:build
```

# Running

## Running with docker

See the [docker-app](docker-app) module for instructions.

## Running outside of docker

For development, you will likely be doing what follows; however, be warned that
we routinely 'hung' our laptops by running in this mode.  Our current
recommendation is to set the number of executions to a small number for test
development and use the docker application for doing the 'real' benchmarking.

# Running a server the HTTP clients will hit

The [mock-application](mock-application) module is a wiremock based application
for stubbing http client benchmark use cases.  The HTTP clients under test will
submit requests to this.

To start the server:

```sh
mvn -pl mock-application compile exec:java
```

### Running the client load tests

Each client goes into an xyz-benchmark module.  To run them, you will be
leverating a Maven profile and Maven's `verify` phase.  For example, to get
reactor-netty's benchmarks, you would do:

```sh
mvn -Pperformance -pl reactornetty-benchmark verify
```

# Configuration

### Number of test runs

By default, it is configured to 10,000.
