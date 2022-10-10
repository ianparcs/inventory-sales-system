FROM openjdk:8-jdk-alpine
ADD target/rmhometiles-0.0.1.jar rmhometiles-0.0.1.jar
ENTRYPOINT ["java","-jar","rmhometiles-0.0.1.jar"]