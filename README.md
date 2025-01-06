# Discography service

# Clara technical challenge
Technical challenge for Clara, Discography service
# Technological stack

- OS MacOS 14
- Java 21 
- Maven 3.9.6
- Spring Boot 3.4.1
- JPA 
- Docker
- Jacoco
- Junit 5
- Lombok
- Swagger 
- Intellij IDEA

### Software architecture and patterns
- Hexagonal architecture (ports and adapters)
- SOLID


# Architecture


The solution is based on a hexagonal architecture (ports and adapters).


###  Ports (Input and Output):

#### application.port.in (Input Port):

#### application.port.out (Output Port):


###  Adapters:

#### adapter.in.rest:

#### adapter.out.jpa:

### Domain:

### Flow:


# API Description

# Running the service locally
## Prerequisites
- Java 21 or later
- Maven

## Optional
- Docker
- Make

## Running the service using maven
```shell
mvn spring-boot:run
```

## Running the service using Makefile ( mandatory Docker and Make )
```shell
make up 
```

# Test 
The service has implemented unit tests, integration tests and system tests.
To run tests run any of the following commands:

```shell
mvn test 
``` 
```shell
make test 
```

After running the tests, the coverage report can be found in the target folder. `target/site/jacoco/index.html`

![My Image](coverage-report.png)

# Test the service
## Locally
Use curl in command line or an application like Postman.
Also, it is possible to test the service using the Swagger UI
```shell

```
## Complementary endpoints
- `http://localhost:8080/h2-console `
H2 Console 
- `http://localhost:8080/swagger-ui/index.html `
Swagger Documentation and Try it out 

## Cloud
  
# To - Do


