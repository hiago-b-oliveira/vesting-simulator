## Vesting Events Processor

### Stack

* Java 11
* Spring Boot 2.6
* [Lombok](https://projectlombok.org/)
* [opencsv](https://www.baeldung.com/opencsv)

*Disclaimer:* To open the project in na IDEA, it's required to install the Lombok plugin first.
See [IDEs setup](https://projectlombok.org/setup/overview).

### Project Overview

This application uses the Spring framework to create a command-line runner and to manage the dependencies with
dependency injection. After receiving the arguments, the app creates a Stream of events to be processed by an
accumulator. The three main steps of turning the CSV file into a Stream, accumulating the Stream, and displaying the
results can be found in the HoCodingTestApplication class.

There are two possible ways to consume the Stream:
sequential or parallel (the default number of threads to consume parallel streams is the same numbers of available
processors minus one)

### Test Cases

In the `IntegrationTest` class we have some tests that run the examples 1, 2, and 3 provided with the problem
description.

### Running the Application

###### (Optional) Install Java using [asdf](https://github.com/asdf-vm/asdf)

```sh
    asdf plugin-add java https://github.com/halcyon/asdf-java.git
    asdf install # it'll read the java version from the .tools file
```

###### Build the app

```sh
  ./mvnw package
```

###### Running the app

```sh
  # Running the command without arguments will display some usage examples
  java -jar ./target/ho-coding-test-0.0.1-SNAPSHOT.jar 
  
  # Running the app with logs and time metrics
  java -jar -Dspring.profiles.active=dev ./target/ho-coding-test-0.0.1-SNAPSHOT.jar  ./samples/example1.csv 2020-03-03
```

###### Application Performance

During the tests it was possible to process a file of 10 million lines and 1 million clients in 29 seconds (339974
events per second)

The benchmark details can be seen [here](./doc/benchmark.md).
