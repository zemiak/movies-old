FROM zemiak/wildfly
MAINTAINER zemiak@gmail.com

ENV DEBIAN_FRONTEND noninteractive
RUN echo deb http://www.deb-multimedia.org jessie main non-free >>/etc/apt/sources.list
RUN apt-get update && apt-get -yq --force-yes install ffmpeg mp4v2-utils \
    ffmpegthumbnailer bzip2

RUN mkdir -p /mnt/media

RUN rm -rf /opt/wildfly/standalone/configuration/standalone*.xml
COPY standalone.xml /opt/wildfly/standalone/configuration/

# Restore the database
RUN mkdir -p /var/lib/movies
COPY movies.plain.bz2 /tmp/
RUN bunzip2 /tmp/movies.plain.bz2
RUN /opt/jdk/bin/java \
    -cp /opt/wildfly/modules/system/layers/base/com/h2database/h2/main/h2-1.3.173.jar \
    org.h2.tools.RunScript -url jdbc:h2:/var/lib/movies/database -user sa -script /tmp/movies.plain
RUN rm /tmp/movies.plain
RUN chown -R wildfly:wildfly /var/lib/movies
RUN chown wildfly:wildfly /var/lib/movies/*

USER wildfly
COPY movies.war /opt/wildfly/standalone/deployments/

USER root
RUN chown -R vasko /opt/wildfly/standalone/*

VOLUME ["/mnt/media/"]

EXPOSE 8080 22
USER root
