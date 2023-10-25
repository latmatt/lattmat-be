FROM openjdk:17-jdk-slim as build

#Information around who maintains the image
MAINTAINER ZAYARLINNNAUNG

# Add the application's jar to the container
COPY target/lattmat-0.0.1-SNAPSHOT.jar lattmat.jar

EXPOSE 443

#execute the application
ENTRYPOINT ["java","-jar","/lattmat.jar"]