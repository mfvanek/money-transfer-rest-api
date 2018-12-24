# Simple implementation of RESTful API for money transfers between accounts.

## Task
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

### Explicit requirements:
1. You can use Java, Scala or Kotlin.
1. Keep it simple and to the point (e.g. no need to implement any authentication).
1. Assume the API is invoked by multiple systems and services on behalf of end users.
1. You can use frameworks/libraries if you like (except Spring), but don't forget about requirement #2 – keep it simple and avoid heavy frameworks.
1. The datastore should run in-memory for the sake of this test.
1. The final result should be executable as a standalone program (should not require a pre-installed container/server).
1. Demonstrate with tests that the API works as expected.

## Technology stack
- Java 8
- [Maven](https://maven.apache.org/)
- [Spark Framework](http://sparkjava.com) (with embedded Jetty)
- [Logback](https://logback.qos.ch)
- [Lombok](https://projectlombok.org)
- [google/gson](https://github.com/google/gson)
- **Hand-written in-memory data storage using concurrency utilities**
- [JUnit 5](https://junit.org/junit5/)
- [Apache HttpClient](https://hc.apache.org/index.html) (for unit testing)

## How to run

## Available services

### Pagination
- [http://localhost:9999/parties?limit=10](http://localhost:9999/parties?limit=10)
- [http://localhost:9999/parties?page=2&limit=20](http://localhost:9999/parties?page=2&limit=20)
- [http://localhost:9999/accounts/1/transactions?limit=100](http://localhost:9999/accounts/1/transactions?limit=100)

## Http status

## Notes
- Using Russian bank accounts
- Decided to ignore topics of **currency conversions** and **multi-currency operations**
