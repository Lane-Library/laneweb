#
# maven maintenance
#
FROM gcr.io/som-laneweb/laneweb:latest AS PREVIOUS_IMAGE
# use .m2 directory from previous image to speed-up build-times
# .m2 should already exist, but create just in case
RUN mkdir -p /root/.m2

#
# build phase
#
FROM gcr.io/som-laneweb/maven-grover:3.6.3 AS MAVEN_TOOL_CHAIN
COPY --from=PREVIOUS_IMAGE /root/.m2 /root/.m2
RUN mkdir -p /build
COPY pom.xml settings.xml /build/
COPY .git /build/.git/
COPY src /build/src/
WORKDIR /build/
RUN mvn -B -s settings.xml clean package
RUN rm -rf .git
# purge maven dependencies that haven't been accessed recently
RUN find /root/.m2/repository -atime +30 -iname '*.pom' \
    | while read pom; do parent=`dirname "$pom"`; rm -Rf "$parent"; done

#
# run phase
#
FROM openjdk:11-jre

RUN apt-get update
    

COPY --from=MAVEN_TOOL_CHAIN /root/.m2 /root/.m2
COPY --from=MAVEN_TOOL_CHAIN /build/target/laneweb.war laneweb.war

VOLUME /config
VOLUME /secrets

ENV SPRING_CONFIG_LOCATION=/secrets/application.properties,/config/application.properties

CMD ["java", "-jar", "laneweb.war"]
