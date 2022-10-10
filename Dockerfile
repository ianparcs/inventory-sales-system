

#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/inventory-sales-system/src
COPY pom.xml /home/inventory-sales-system
RUN mvn -f /home/inventory-sales-system/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/inventory-sales-system/target/rmhometiles-0.0.1.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]


FROM ubuntu:22.04
RUN apt-get update -y
