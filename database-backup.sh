#!/bin/sh

java -cp ~/bin/wildfly/modules/system/layers/base/com/h2database/h2/main/h2-1.3.173.jar org.h2.tools.Script -url jdbc:h2:/tmp/movies -user sa -script ./src/dev/resources/dump.sql
