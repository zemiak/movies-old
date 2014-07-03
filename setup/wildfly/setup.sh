#!/bin/bash

MAC=/Users/vasko/bin/wildfly8/
LINUX=/home/wildfly/wildfly8/
WINDOWS=/e/Java/wildfly8/

if [ -d "$MAC" ]
then
    export JBOSS_HOME="$MAC"
elif [ -d "$LINUX" ]
then
    export JBOSS_HOME="$LINUX"
else
    export JBOSS_HOME="$WINDOWS"
fi



CLI="${JBOSS_HOME}bin/jboss-cli.sh"

if [ ! -x "$CLI" ]
then
    echo "Wildfly command line $JBOSS_HOME not found"
    exit 1
fi

if [ -z "$1.cli" ]
then
    echo "Provide environment name (dev, prod...)"
    exit 2
fi

$CLI --file=driver-install.cli
$CLI --file=common.cli
$CLI --file="$1-properties.cli"
$CLI --file="$1-ds.cli"
