#!/usr/bin/env bash
export remoteBase="/root/src/micro-service"
export remoteHost="dev1"

ssh root@${remoteHost}<<EOF
    cd ${remoteBase}

    mkdir -p bin
    exit
EOF

scp ../../pom.xml root@${remoteHost}:${remoteBase}/
scp ../../bin/*.sh root@${remoteHost}:${remoteBase}/bin
scp ../../bin/warehouse/* root@${remoteHost}:${remoteBase}/bin

scp2dev1-module.sh "config-service registry-service auth-service account-service edge-service warehouse-service monitor-dashboard"
