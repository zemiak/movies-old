#!/bin/sh

if [ ! -f ../../dev/resources/movies.plain.bz2 ]
then
    echo Run from src/main/setup folder
    exit 1
fi

rm ../../dev/resources/movies.plain.bz2
java -cp ~/bin/wildfly/modules/system/layers/base/com/h2database/h2/main/h2-1.3.173.jar org.h2.tools.Script -url jdbc:h2:/tmp/movies -user sa -script ../../dev/resources/movies.plain
bzip2 ../../dev/resources/movies.plain
