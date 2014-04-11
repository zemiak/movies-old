#!/bin/bash

MAC=/Applications/NetBeans/glassfish-4.0/glassfish/bin/asadmin
LINUX=/home/glassfish/glassfish4/glassfish/bin/asadmin

if [ -f "$MAC" ]
then
    COMMAND="$MAC"
else
    COMMAND="$LINUX"
fi

if [ ! -f "$COMMAND" ]
then
    echo "Glassfish command line $COMMAND not found"
    exit 1
fi

if [ ! -f "$1.xml" ]
then
    echo "$0 environment"
fi

$COMMAND add-resources --port 4848 "$1.xml"
