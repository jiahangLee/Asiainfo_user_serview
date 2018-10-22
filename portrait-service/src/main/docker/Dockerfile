FROM iron/java

ADD portrait-service-1.0-SNAPSHOT.jar portrait-service.jar
EXPOSE 9999
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/portrait-service.jar"]