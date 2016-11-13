FROM zemiak/payara
MAINTAINER zemiak@gmail.com

COPY postgresql-9.4.1212.jar $PAYARA_HOME/domains/$DOMAIN_NAME/lib/

RUN asadmin start-domain ${DOMAIN_NAME} && \
    echo "AS_ADMIN_PASSWORD=${ADMIN_PASSWORD}" > payarapwd && \
    asadmin --passwordfile=payarapwd create-jdbc-connection-pool --datasourceclassname org.postgresql.xa.PGXADataSource --restype javax.sql.XADataSource --property user=postgres:serverName=database:databaseName=movies:port=5432:password=\"\" movies_pool && \
    asadmin --passwordfile=payarapwd create-jdbc-resource --connectionpoolid movies_pool movies_db && \
    asadmin --passwordfile=payarapwd create-javamail-resource --mailuser none --fromaddress noreply@nowhere.com --mailhost mail mail/movies && \
    asadmin --passwordfile=payarapwd stop-domain ${DOMAIN_NAME} && \
    mkdir -p /mnt/media

VOLUME ["/mnt/media"]

COPY movies.war $DEPLOYMENT_DIR
COPY start.sh /usr/local/bin/

CMD sh /usr/local/bin/start.sh
