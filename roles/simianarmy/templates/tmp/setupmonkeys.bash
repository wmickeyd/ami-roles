#!/bin/bash

#Should be using IAM instance roles
#export AWS_ACCESS_KEY_ID=simian_access_key_id
#export AWS_SECRET_ACCESS_KEY=simian_secret_access_key

export JAVA_HOME=/{{ java_root }}/jdk8
echo "Java home came out looking like: $JAVA_HOME"
export WAR_LOCATION=/{{ kenzanmedia_apps }}/webapps

SIMIAN_ARMY_SDB_DOMAIN={{ simian_army_sdb_domain }}

echo ""

#Check for the SimpleDB domain before trying to create it
echo "SimpleDB domain list:"
sdbadmin -l

sdbadmin -l | grep $SIMIAN_ARMY_SDB_DOMAIN
if [ $? -eq 1 ]; then
  echo "$SIMIAN_ARMY_SDB_DOMAIN not found, creating"
  sdbadmin -c $SIMIAN_ARMY_SDB_DOMAIN
fi


cd /{{ simian_army_location }}/SimianArmy
./gradlew -x test build

cp build/libs/simianarmy*.war $WAR_LOCATION/simianarmy.war
