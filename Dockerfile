#
# maven maintenance
#
FROM gcr.io/som-laneweb/jre-parent:eclipse-temurin-11.0.16.1_1-jre
COPY target/laneweb.war laneweb.war


ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]
