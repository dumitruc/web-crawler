# README #

This is a basic web crawler, takes one root page and navigate any other pages under same hostname.

### What is this repository for? ###

* Java coding exercise

### How do I get set up? ###

* Java SDK (8 onwards)
* Maven

# To use
Build the JAR of the application
```shell
mvn clean install
```

Build docker image
```shell
docker build -t web-crawler .
```

Run app on docker
```shell
docker run --rm web-crawler "https://whatever...."
```