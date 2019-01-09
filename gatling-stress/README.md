# Introduction

This module is used to quickly test the server to make sure verify it can handle
the load we are attempting with our clients.  We believe Gatling is moderately
popular in the JVM industry for load testing.

Credit to this [sample](https://github.com/gatling/gatling-maven-plugin-demo).

# Usage

Assuming your server's base URL is 'http://localhost:8080', you can:

```
mvn gatling:test
```

Adjust the code to hit other hosts/ports.