FROM iron/java

ADD config-service-1.0-SNAPSHOT.jar config-service.jar
EXPOSE 8888
VOLUME /serviceConfig
ENTRYPOINT ["java", "-cp", "./serviceConfig", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/config-service.jar"]