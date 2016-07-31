# tomcat sidecar cmd

# copy laneweb.war to the emptyDir volume path, i.e. /tomcat/webapps
mkdir -p /tomcat/webapps
cp /laneweb.war /tomcat/webapps
# hold the container with a tail forever
tail -f /dev/null