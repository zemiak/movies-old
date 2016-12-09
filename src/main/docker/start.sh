#!/bin/sh

sh /usr/local/bin/start-payara.sh

echo $BIN_PATH
echo $MEDIA_PATH
echo $SYSTEM_NAME
echo $MAIL_TO
echo $EXTERNAL_URL

tail -f /etc/hosts
