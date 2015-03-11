"$JAVA_HOME/bin/java" -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4002,suspend=n -jar target/mmc-rest-0.0.1-SNAPSHOT.jar server --logging.config=../../mmc-config/mmc-rest/dev_local/logback.xml --spring.config.location=src/test/resources/application.yml,../../configuration.yml

curl http://localhost:4001/admin/health
