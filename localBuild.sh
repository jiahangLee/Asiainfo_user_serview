#!/usr/bin/env bash

base=$(pwd)
deployDir=/tmp/gags
mkdir -p $deployDir
echo "base=$base"
deployModel(){

    model=$1
    cd ${base}/${model}
    mvn package -DskipDockerBuild
    cd ${base}
    mkdir -p ${deployDir}/${model}
    cp ${model}/target/${model}-1.0-SNAPSHOT.jar ${deployDir}/${model}/
    cp ${model}/src/main/resources/application.yml ${deployDir}/${model}/
    cp ${model}/run.sh ${deployDir}/${model}/
    chmod 755 ${deployDir}/${model}/run.sh
}

for m in config-service registry-service auth-service account-service edge-service analyse-service monitor-dashboard;
do
    deployModel $m
done