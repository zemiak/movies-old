#!/bin/bash

MAC=/Applications/NetBeans.app/glassfish-4.0/glassfish/bin/asadmin
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

if [ -z "$1" ]
then
    echo "$0 glassfish-resources-file.xml"
fi

$COMMAND add-resources --port 4848 "$1"
