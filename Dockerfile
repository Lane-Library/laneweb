#
# maven maintenance
#
FROM gcr.io/som-laneweb/laneweb:latest AS PREVIOUS_IMAGE
COPY target/laneweb.war laneweb.war


ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]
