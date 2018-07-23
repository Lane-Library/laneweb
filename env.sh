# This project
export GCP_PROJECT_ID=som-laneweb
export GCP_PROJECT_NAME=${GCP_PROJECT_ID}
export GCP_CONFIGURATION=${GCP_PROJECT_NAME}-${GCP_ENVIRONMENT}
export GCP_REGION=us-west1
export GCP_ZONE=${GCP_REGION}-a
export GCP_NAT_TAGS=nat-stanford

# Vault and secrets configuration
export VAULT_ADDR=https://vault.med.stanford.edu
export VAULT_AUTH_METHOD=ldap
export VAULT_CACHE=${HOME}/.vault-local
export SEC_PATH=secret/projects/${GCP_PROJECT_NAME}

# PS Cloud Framework (Scripts, shared config, etc.)
export FRAMEWORK_DIR=${HOME}/bin/ps-cloud-framework
export FRAMEWORK_BUCKET=ps-cloud-framework
export SCRIPTS_DIR=${FRAMEWORK_DIR}/scripts

# Docker configuration
export DOCKER_IMAGE=laneweb
export DOCKER_REGISTRY=gcr.io
export DOCKER_REGISTRY_USERNAME=_json_key
export DOCKER_REGISTRY_PASSWORD_PATH=${SEC_PATH}/common/gcr-user

# Drone configuration
export DRONE_SERVER=https://ci.med.stanford.edu
export DRONE_TOKEN_FILE=${HOME}/.drone-token
export DRONE_REPO=lane/laneweb
export DRONE_SEC_FILE=.drone.sec
export DRONE_REGISTRY_FILE=.drone.reg
