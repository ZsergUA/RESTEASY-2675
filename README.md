###### RESTEasy issue https://issues.redhat.com/browse/RESTEASY-2675 demonstration

Simple demonstration of regression in RESTEasy 3.x/4.x up to 3.13.0.Final/4.5.6.Final

Project contains 1 simple test and 3 maven profiles to run in against RESTEasy 2.3.10.Final, 3.13.0.Final, 4.5.6.Final

Use this commands to verify each version:
```
./mvnw clean test -Presteasy-2
./mvnw clean test -Presteasy-3
./mvnw clean test -Presteasy-4
```
