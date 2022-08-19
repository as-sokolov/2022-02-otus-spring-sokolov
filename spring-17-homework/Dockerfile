FROM maven:3.8.6-openjdk-11-jre

ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

ADD ./pom.xml $PROJECT_DIR
RUN mvn dependency:resolve

ADD ./README.md $PROJECT_DIR
ADD ./src/ $PROJECT_DIR/src
RUN mvn install

FROM openjdk:11-jdk
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=0 $PROJECT_DIR/target/spring-17-homework* $PROJECT_DIR/
EXPOSE 8080
CMD ["java", "-jar", "/opt/project/spring-17-homework-0.0.1-SNAPSHOT.jar"]