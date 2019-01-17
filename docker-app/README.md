# Introduction

This builds a docker image that contains both the client tests and the mock
server.

## WARNING!!!

Some of this code ASSumes the other modules are using certain conventions.

# Usage

The application has help.

```sh
docker run --rm -it crankydillo/crankydillo/http-client-benchmark --help
```

## Running server and client tests locally on a 2015-ish Mac

I'd use `-net=host` on linux.

```sh
docker run --name server \
    -p 8080:8080 \
    -it --rm \
    crankydillo/http-client-benchmark \
    server

MET_DIR=/FILL_IN; docker run \
  -v $MET_DIR:/metrics -e BM.METRICS.DIR=/metrics \
  --link server:svr -ti --rm \
  crankydillo/http-client-benchmark \
  test all svr 8080 10000 40

MET_DIR=/FILL_IN; docker run \
  -v $MET_DIR:/reportdir -it --rm \
  crankydillo/http-client-benchmark \
  report /reportdir > out.html
  
open out.html
```

Of course, feel free to do whatever you want with directory names.

# Customization of the `test` sub-command

If you need customization (e.g. specifying Java's -Xmx value) beyond what the
arguments give you, you'll need to override the container's entrypoint.

For the `test` subcommand, I output the `java` command that I use to launch
TestNG.  It can be overridden like this:

```sh
docker run --rm -it --entrypoint java crankydillo/http-client-benchmark \
    -Xmx6G \
    -Dbm.host=some.host -Dbm.port=8080 \
    -Dbm.test.executions=10 -Dbm.test.workers=2 \
    -Dbm.dropwizard.seconds=30 \
    -jar docker-clients/lib/rxnetty-benchmark-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
    testng/rxnetty.xml
```

### And getting real hacky

_If_ you want, you can treat the container like a shell.  For example:

```sh
docker run --rm -it --entrypoint bash crankydillo/http-client-benchmark 
java -Dbm.host=some.host \
    -Dbm.test.executions=2 -Dbm.dropwizard.seconds=15 \
    -jar docker-clients/lib/reactornetty-benchmark-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
    testng/reactornetty.xml \
    -methods benchmark.reactornetty.PerformanceTests.testBlockingShortGET
```

# Building

Build the entire project.  From the root directory, run:

```sh
mvn clean install
```

Then build the docker container.  In this module's directory, run:

```sh
mvn package docker:build
```
