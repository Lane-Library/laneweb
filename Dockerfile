FROM us-docker.pkg.dev/som-laneweb/docker-private/jre-parent:eclipse-temurin-19-jre
COPY target/laneweb.war laneweb.war

ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]
