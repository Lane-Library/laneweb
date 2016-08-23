From busybox:latest

ADD mv.sh mv.sh
ADD target/laneweb.war ROOT.war

VOLUME /tomcat/webapps

CMD "sh" "mv.sh"
