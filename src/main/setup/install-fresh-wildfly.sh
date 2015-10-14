#!/bin/sh

WILDFLY=wildfly-10.0.0.CR2
PROJECT=~/Documents/projects/movies
DOWNLOAD=~/Downloads/$WILDFLY.zip
INSTALL=~/bin
TARGET=wildfly10

cd $INSTALL
killall java
test -d $TARGET && rm -rf $TARGET
cp $DOWNLOAD ./
unzip $WILDFLY.zip >/dev/null
rm $WILDFLY.zip
mv $WILDFLY $TARGET
cd $TARGET/standalone/configuration
rm standalone-*.xml
ln -s standalone.xml standalone-full.xml
ln -s standalone.xml standalone-full-ha.xml
ln -s standalone.xml standalone-ha.xml
cd ../../..

./$TARGET/bin/add-user.sh vasko 123456 --silent
./$TARGET/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &
sleep 5s

cd $PROJECT/src/main/setup
sh setup.sh

sleep 3s
killall java
echo "Finished!"
