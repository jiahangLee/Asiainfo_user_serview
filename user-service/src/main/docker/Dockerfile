FROM iron/java

ADD user-service-1.0-SNAPSHOT.jar user-service.jar
EXPOSE 8899
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/user-service.jar"]