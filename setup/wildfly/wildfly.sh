#!/bin/bash

MAC=/Users/vasko/bin/wildfly8/
LINUX=/home/wildfly/wildfly8/

if [ -d "$MAC" ]
then
    export JBOSS_HOME="$MAC"
else
    export JBOSS_HOME="$LINUX"
fi

CLI="${JBOSS_HOME}bin/jboss-cli.sh"

if [ ! -x "$CLI" ]
then
    echo "Wildfly command line $JBOSS_HOME not found"
    exit 1
fi

if [ -z "$1" ]
then
    echo "Provide configuration CLI file"
    exit 2
fi

$CLI --file="$1"
