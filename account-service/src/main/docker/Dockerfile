FROM iron/java

ADD account-service-1.0-SNAPSHOT.jar account-service.jar
EXPOSE 6000
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/account-service.jar"]