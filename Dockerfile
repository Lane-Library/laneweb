From busybox:latest

ARG PROJECT_VERSION

ADD mv.sh mv.sh
ADD target/laneweb-${PROJECT_VERSION}.war laneweb.war

VOLUME /app

CMD "sh" "mv.sh"