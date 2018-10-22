#!/usr/bin/env bash
docker image save mysql > mysql.tar
docker image save rabbitmq > rabbitmq.tar
for m in "config-service registry-service auth-service account-service edge-service analyse-service monitor-dashboard";
do
docker image save gags/$m >$m.tar
done
