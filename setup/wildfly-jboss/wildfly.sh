#!/bin/bash

MAC=/Users/vasko/bin/wildfly8/
LINUX=/home/wildfly/wildfly8/

if [ -d "$MAC" ]
then
    export JBOSS_HOME="$MAC"
else
    export JBOSS_HOME="$LINUX"
fi

if [ ! -d "$JBOSS_HOME" ]
then
    echo "Wildfly home directory $JBOSS_HOME not found"
    exit 1
fi

if [ -z "$1" ]
then
    echo "$0 wildfly-resources-file.xml"
fi

