#!/usr/bin/env bash
java -jar registry-service-1.0-SNAPSHOT.jar --spring.config.location=./application.yml --spring.profiles.active=swagger-ui,docker