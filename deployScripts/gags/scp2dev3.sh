#!/usr/bin/env bash
export remoteBase="/root/src/micro-service"
export remoteHost="dev3"

ssh root@${remoteHost}<<EOF
    cd ${remoteBase}

    mkdir -p bin/serviceConfig

    exit
EOF
scp ../../pom.xml root@${remoteHost}:${remoteBase}/
scp ../../bin/*.sh root@${remoteHost}:${remoteBase}/bin
scp ../../bin/gags/* root@${remoteHost}:${remoteBase}/bin
scp2dev3-module.sh "config-service registry-service auth-service account-service edge-service analyse-service monitor-dashboard"

