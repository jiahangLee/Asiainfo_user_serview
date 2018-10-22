FROM iron/java

ADD auth-service-1.0-SNAPSHOT.jar auth-service.jar
EXPOSE 5000
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/auth-service.jar"]