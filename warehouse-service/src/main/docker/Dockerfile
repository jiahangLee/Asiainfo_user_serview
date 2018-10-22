FROM iron/java
ADD warehouse-service-1.0-SNAPSHOT.jar warehouse-service.jar
EXPOSE 9900
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/warehouse-service.jar"]