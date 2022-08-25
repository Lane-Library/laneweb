#
# maven maintenance
#
FROM gcr.io/som-laneweb/jre-parent:openjdk-11.0.16-jre-slim
COPY target/laneweb.war laneweb.war


ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]
