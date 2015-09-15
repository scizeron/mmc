"$JAVA_HOME/bin/java" -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4002,suspend=n -jar target/mmc-api.jar server --logging.config=src/test/resources/logback.xml --spring.config.location=src/test/resources/application.yml,../../configuration.yml

