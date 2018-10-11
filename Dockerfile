FROM openjdk:11-jre

RUN apt-get update && \
    apt-get -y install \
    fontconfig \
    ttf-dejavu

COPY target/laneweb.war laneweb.war

VOLUME /config
VOLUME /secrets

ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["java", "-jar", "laneweb.war"]
