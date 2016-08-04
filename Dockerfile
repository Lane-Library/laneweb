From busybox:latest

ARG PROJECT_VERSION

ADD mv.sh mv.sh
ADD target/laneweb-${PROJECT_VERSION}.war ROOT.war

VOLUME /tomcat/webapps

CMD "sh" "mv.sh"