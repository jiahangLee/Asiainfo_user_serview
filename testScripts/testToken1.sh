#!/usr/bin/env bash

ret=`curl -d "client_id=browser&client_secret=android-ops&grant_type=password&username=admin&password=123456" http://localhost:8801/uaa/oauth/token`
token=`echo $ret|awk -F \" '/access_token/ {print $4}'`
echo "token is $token"
curl -v -H "Authorization: Bearer $token" http://localhost:8801/uaa/user
curl -v -H "Authorization: Bearer $token" http://localhost:8800/api/v1/auth/uaa/user

