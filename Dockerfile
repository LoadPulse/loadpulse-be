FROM openjdk:19-alpine
EXPOSE 8080

WORKDIR /usr/app

COPY web/target/web-0.0.1-SNAPSHOT.jar /usr/app

CMD ["java", "-jar", "web-0.0.1-SNAPSHOT.jar"]