
#
# Build stage
#
#FROM maven:3.6.0-jdk-11-slim AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/inventory-sales-system/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY target/rmhometiles-0.0.1.jar rmhometiles-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/rmhometiles-0.0.1.jar"]


# the script we want to run when the program is booting
# notice, the entrypoint is defined as array
FROM ubuntu:22.04
RUN apt-get update -y
