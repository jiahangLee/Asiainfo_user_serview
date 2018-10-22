#!/usr/bin/env bash

ret=`curl -d "client_id=browser&grant_type=password&scope=ui&username=admin&password=7c4a8d09ca3762af61e59520943dc26494f8941b" http://ali2:4000/uaa/oauth/token`
echo $ret
token=`echo $ret|awk -F \" '/access_token/ {print $4}'`
echo "token is $token"
refreshToken=`echo $ret|awk -F \" '/refresh_token/ {print $12}'`
echo "refreshtoken is $refreshToken"
ret=`curl -d "client_id=browser&grant_type=refresh_token&scope=ui&refresh_token=$refreshToken" http://ali2:4000/uaa/oauth/token`
echo $ret
token=`echo $ret|awk -F \" '/access_token/ {print $4}'`

curl  -H "Authorization: Bearer $token" http://ali2:4000/uaa/users/current
echo ""
curl  -H "Authorization: Bearer $token" http://ali2:4000/accounts/users
echo ""
curl  -H "Authorization: Bearer $token" http://ali2:4000/accounts/current
echo ""
curl  -H "Authorization: Bearer $token" http://ali2:4000/accounts/users/admin
echo ""
curl  -H "Content-Type: application/json" -H "Authorization: Bearer $token" -d "{\"name\":\"abc\",\"password\":\"123\"}" http://ali2:4000/accounts/users
echo ""
curl  -X DELETE -H "Authorization: Bearer $token" http://ali2:4000/accounts/users/2
echo ""
curl  -H "Authorization: Bearer $token" http://ali2:4000/warehouses/airMaterials

