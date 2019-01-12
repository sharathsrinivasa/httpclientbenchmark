# Introduction

This builds a docker image that contains both the client tests and the mock
server.

# Usage

The application has help.

```sh
docker run --rm -it crankydillo/crankydillo/http-client-benchmark --help
```

# Customization of the `test` subcommand

If you need customization (e.g. specifying Java's -Xmx value) beyond what the
arguments give you, you'll need to override the container's entrypoint.

For the `test` subcommand, I output the `java` command that I use to launch
TestNG.  It can be overridden like this:

```sh
docker run --rm -it --entrypoint java crankydillo/http-client-benchmark \
-Xmx6G \
-Dbm.host=10.176.14.217 -Dbm.port=8080 \
-Dbm.test.executions=10 -Dbm.test.workers=2 \
-Dbm.dropwizard.seconds=30 \
-jar docker-clients/lib/rxnetty-benchmark-1.0.0-SNAPSHOT-jar-with-dependencies.jar \
testng/rxnetty.xml
```
