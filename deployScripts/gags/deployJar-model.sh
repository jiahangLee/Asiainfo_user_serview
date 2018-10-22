#!/usr/bin/env bash


export remoteBase="/root/src/micro-service"

export remoteHost="dev3"


deployModel(){
    model=$1
    echo "deploy ${model} to ${remoteHost}"

ssh root@${remoteHost}<<EOF
    cd ${remoteBase}/bin/deploy
    rm -rf ${model}
    mkdir ${model}
    cp ${remoteBase}/${model}/target/${model}-1.0-SNAPSHOT.jar ${model}/
    cp ${remoteBase}/${model}/src/main/resources/application.yml ${model}/
    cp ${remoteBase}/${model}/run.sh ${model}/
    chmod 755 run.sh
    exit
EOF
}



ssh root@${remoteHost}<<EOF
    cd ${remoteBase}/bin
    mkdir -p deploy
    exit
EOF


for m in $1;
do
    deployModel $m
done

