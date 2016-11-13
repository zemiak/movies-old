#!/bin/sh

rm -rf /opt/payara41/glassfish/domains/domain1/logs
mkdir -p /mnt/media/logs/movies
ln -s /mnt/media/logs/movies /opt/payara41/glassfish/domains/domain1/logs
sh /usr/local/bin/start-payara.sh

tail -f /etc/hosts
