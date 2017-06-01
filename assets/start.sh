#!/bin/bash -e 

# Tomcat sidecar jobs
# 1. Copy laneweb.war to the shared /base/webapps
# 2. Sync content from git repo to /content on given time interval

# webapps dir
WEBAPP_DIR=${WEBAPP_DIR-/base/webapps}
# content git repo 
REPO=${REPO:-"git@gitlab.med.stanford.edu:irt-lane/laneweb-content.git"}
# destination dir
DEST=${DEST:-/content}
# git repo deploy key
DEPLOY_KEY=${DEPLOY_KEY:-/keys/id_rsa}
# pull interval in seconds
INTERVAL=${INTERVAL:-300}

# 1. Copy laneweb.war to the shared /base/webapps
mkdir -p ${WEBAPP_DIR}
cp /ROOT.war ${WEBAPP_DIR}

# 2. Sync content from git repo to /content on given time interval
GIT_USER=${GIT_USER:-Docker Content}
GIT_EMAIL=${GIT_USER:-docker-content@exmaple.com}
git config --global user.name ${GIT_USER}
git config --global user.email ${GIT_EMAIL}

# Setup git deployment key
mkdir -p $HOME/.ssh/
cp $DEPLOY_KEY $HOME/.ssh/id_rsa
chmod 400 $HOME/.ssh/id_rsa
# git ssh cmd
# why options, see https://www.joedog.org/2012/07/13/ssh-disable-known_hosts-prompt
export GIT_SSH_COMMAND="ssh -o 'StrictHostKeyChecking=no' -o 'UserKnownHostsFile=/dev/null'"

if [ ! -d "${DEST}" ]; then mkdir -p ${DEST}; fi
cd $DEST

while true
do
	if [ ! -d ".git" ]
	then
		git init
		git remote add origin ${REPO}
	fi
	git fetch origin master --depth=1
	git reset --hard origin/master
	sleep ${INTERVAL}
done
