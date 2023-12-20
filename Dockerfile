FROM us-docker.pkg.dev/som-laneweb/docker-private/jre-parent:eclipse-temurin-11.0.21_9-jre
COPY target/laneweb.war laneweb.war

ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]
