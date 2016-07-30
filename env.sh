# This project
export PROJECT=${IMAGE}
export PROJECT_ID=som-lane-dev
export SCRIPTS_DIR=scripts
export SCRIPTS_REPO=https://code.stanford.edu/irt-dcs/kube-scripts.git

export IMAGE=laneweb
export REGISTRY=gcr.io
export REGISTRY_USER=_json_key
export REGISTRY_EMAIL=$(whoami)@stanford.edu

export REGISTRY_KEY_FILE="../accounts/som-lane-dev/gcr.json"
export SLACK_URL_FILE="../accounts/som-lane-dev/slack.key"
export SLACK_CHANNEL="irt-dcs-cicd"
export DRONE_TOKEN_FILE="../accounts/som-lane-dev/drone.key"

export VERSION=$(git rev-parse --verify HEAD)
export DRONE_SERVER=https://drone.med.stanford.edu
export DRONE_REPO=irt-lane/laneweb
