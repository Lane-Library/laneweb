#!/bin/bash -e 

# Tomcat sidecar jobs
# 1. Copy laneweb.war to the shared /base/webapps
# 2. Sync content from git repo to /content on given time interval

# webapps dir
WEBAPP_DIR=${WEBAPP_DIR-/base/webapps}

# 1. Copy laneweb.war to the shared /base/webapps
mkdir -p ${WEBAPP_DIR}
cp /ROOT.war ${WEBAPP_DIR}

# keep container alive
tail -f /dev/null