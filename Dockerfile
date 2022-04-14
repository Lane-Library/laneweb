#
# maven maintenance
#
FROM gcr.io/som-laneweb/jre-parent:prod-latest
COPY target/laneweb.war laneweb.war


ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]
