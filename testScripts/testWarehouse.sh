#!/usr/bin/env bash

ret=`curl -d "client_id=browser&grant_type=password&scope=ui&username=admin&password=123456" http://localhost:4000/uaa/oauth/token`
token=`echo $ret|awk -F \" '/access_token/ {print $4}'`
echo "token is $token"
curl  -H "Authorization: Bearer $token" http://localhost:4000/warehouses/airMaterials

