FROM iron/java

ADD edge-service-1.0-SNAPSHOT.jar edge-service.jar
EXPOSE 4000
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/edge-service.jar"]