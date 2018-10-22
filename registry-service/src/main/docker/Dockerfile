FROM iron/java

ADD registry-service-1.0-SNAPSHOT.jar registry-service.jar

EXPOSE 8761
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/registry-service.jar"]