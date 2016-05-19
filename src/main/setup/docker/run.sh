#!/bin/sh

VERSION=$1
if [ -z "$VERSION" ]
then
    VERSION=3_2_0
fi

mkdir -p /mnt/media/infuse
docker run -d -p 8080:8080 -p 2200:22 \
    --name=movies \
    -v /mnt/media:/mnt/media \
    movies:$VERSION
