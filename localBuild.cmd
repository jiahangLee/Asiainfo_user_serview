
set target=/tmp
mkdir %target%
for %%m in ("config-service" "registry-service" "auth-service" "account-service" "edge-service" "analyse-service" "monitor-dashboard)
do
   cd %%m
   mvn package -DskipDockerBuild
   mkdir %target%/%%m
   copy target/%%m-1.0-SNAPSHOT.jar %target%/%%m/
   copy target/%%m-1.0-SNAPSHOT.jar %target%/%%m/
   copy src/main/resources/application.yml %target%/%%m/
   copy run.sh %target%/%%m/
   cd ..
