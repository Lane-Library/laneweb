FROM alpine:3.4

RUN apk add --no-cache bash git openssh 

COPY assets/start.sh /start.sh
COPY target/laneweb.war ROOT.war

# volume to be shared with tomcat
VOLUME /tomcat/webapps
VOLUME /content

# volume for git deploy keys
VOLUME /keys

CMD ["/start.sh"]
