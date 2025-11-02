#
# Package stage
#
FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/module-02-final-project-1.0.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
