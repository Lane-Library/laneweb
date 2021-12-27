# Docker configuration
export DOCKER_IMAGE=laneweb
export DOCKER_REGISTRY=gcr.io
export DOCKER_REGISTRY_USERNAME=_json_key
export DOCKER_REGISTRY_PASSWORD_PATH=${SEC_PATH}/common/gcr-user

# Drone configuration
export DRONE_REPO=lane/laneweb

# GITLAB CI configuration
export GITLAB_SERVER=https://gitlab.med.stanford.edu
export GITLAB_REPO=lane/${DOCKER_IMAGE}

# SLACK
export SLACK_WEBHOOK_PATH=${SEC_PATH}/slack/irt-dcs-cicd
export SLACK_GITLAB_CHANNEL=tds-ps-git-commits
export SLACK_CICD_CHANNEL=irt-dcs-cicd

# Sonarqube server url
export SONARQUBE_SERVER=https://sonarqube.med.stanford.edu
