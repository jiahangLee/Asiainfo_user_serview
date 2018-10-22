FROM iron/java

ADD monitor-dashboard-1.0-SNAPSHOT.jar monitor-dashboard.jar
EXPOSE 8000
ENTRYPOINT ["java", "-Xmx128M","-Djava.security.egd=file:/dev/./urandom","-jar","/monitor-dashboard.jar"]