# command:  docker build --tag spring-18-homework-publisher .

FROM maven:3.8.6-openjdk-11-slim

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD pom.xml $PROJECT_DIR
RUN mvn dependency:resolve

ADD ../README.md $PROJECT_DIR
ADD src $PROJECT_DIR/src
RUN mvn clean install

FROM openjdk:11-jdk-slim
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=0 $PROJECT_DIR/target/spring-18-homework-publisher* $PROJECT_DIR/
COPY --from=0 $PROJECT_DIR/target/libs/* $PROJECT_DIR/libs/

EXPOSE 8040
CMD ["java", "-jar", "/opt/project/spring-18-homework-publisher-0.0.1-SNAPSHOT.jar"]