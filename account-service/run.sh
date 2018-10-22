#!/usr/bin/env bash
java -jar config-service-1.0-SNAPSHOT.jar --spring.config.location=./application.yml --spring.profiles.active=swagger-ui,docker