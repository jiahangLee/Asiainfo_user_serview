#!/usr/bin/env bash


export remoteBase="/root/src/micro-service"
export remoteHost="dev3"

deployModel(){
    model=$1
    echo "deploy ${model} to ${remoteHost}"

ssh root@${remoteHost}<<EOF
    cd ${remoteBase}
    rm -rf ${model}
    mkdir ${model}
    exit
EOF
scp -r build.sh ../../${model}/src ../../${model}/pom.xml ../../${model}/run.sh root@${remoteHost}:${remoteBase}/${model}/
scp ../../${model}/src/main/resources/application.yml root@${remoteHost}:${remoteBase}/bin/serviceConfig/${model}.yml

ssh root@${remoteHost}<<EOF
    cd ${remoteBase}/${model}
    chmod 755 build.sh
    ./build.sh
    exit
EOF
}






for m in $1;
do
    deployModel $m
done

