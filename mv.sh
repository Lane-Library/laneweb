# tomcat sidecar cmd

# copy laneweb.war to the emptyDir volume path, i.e. /app
cp /laneweb.war /app
# hold the container with a tail forever
tail -f /dev/null