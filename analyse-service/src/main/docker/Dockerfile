FROM iron/java

ADD analyse-service-1.0-SNAPSHOT.jar analyse-service.jar
EXPOSE 9999
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/analyse-service.jar"]