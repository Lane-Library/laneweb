FROM us-docker.pkg.dev/som-laneweb/docker-private/jre-parent:eclipse-temurin-21_35-jre
COPY target/laneweb.war laneweb.war

ENV SPRING_CONFIG_ADDITIONAL_LOCATION=/secrets/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.war"]