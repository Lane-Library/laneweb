FROM us-docker.pkg.dev/som-laneweb/docker-private/jre-parent:eclipse-temurin-21.0.9_10-jre@sha256:88be2f6d63764f9c1e691e1df68e876fc4b33497a9b77a780d0ddf4f795cac98
COPY target/laneweb.jar laneweb.jar


ENV SPRING_CONFIG_ADDITIONAL_LOCATION=/secrets/application.properties

CMD ["/usr/bin/tini", "--","java", "-jar", "laneweb.jar"]
