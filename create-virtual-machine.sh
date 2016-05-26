#!/bin/sh

docker-machine stop default
docker-machine rm -f default
docker-machine create --driver virtualbox default
docker-machine ssh default "tce-load -wi sshfs-fuse && mkdir media"

cd ../docker/containers/templates/debian || exit 2
docker build --tag=zemiak/debian .
cd ../java
docker build --tag=zemiak/java .
cd ../wildfly
docker build --tag=zemiak/wildfly .
cd ../../../../movies

echo .
echo .
echo .
echo .
echo .
echo Now run
echo .
echo sudo sshfs vasko@192.168.2.10:/mnt/media media -o allow_other -o idmap=user
echo .
echo .
echo .
echo .
echo .

docker-machine ssh default
