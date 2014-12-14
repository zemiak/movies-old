#!/bin/bash

WILDFLY=wildfly-8.2.0.Final
PROJECT=~/Documents/projects/movies
DOWNLOAD=~/Downloads/$WILDFLY.zip
INSTALL=~/bin
TARGET=wildfly8

cd $PROJECT
mvn clean package

cd $INSTALL
killall java
test -d $TARGET && rm -rf $TARGET
cp $DOWNLOAD ./
unzip $WILDFLY.zip >/dev/null
rm $WILDFLY.zip
mv $WILDFLY $TARGET

./$TARGET/bin/add-user.sh vasko 123456 --silent
./$TARGET/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &
sleep 5s

cd $PROJECT/setup/wildfly
bash setup.sh dev

cd ~/bin
./$TARGET/bin/jboss-cli.sh --connect --command="deploy ~/Documents/projects/movies/target/movies.war --force --runtime-name=movies"
