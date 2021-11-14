FROM openjdk:11

COPY target/web-crawler-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/bin/myapp/web-crawler.jar
WORKDIR /usr/bin/myapp

ENTRYPOINT ["java", "-jar", "web-crawler.jar"]
CMD ["DEFAULT-ARG"]

