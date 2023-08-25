FROM arm64v8/openjdk:17
VOLUME /tmp
ARG JAR_FILE=./app/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV USE_PROFILE default
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-jar","/app.jar"]
