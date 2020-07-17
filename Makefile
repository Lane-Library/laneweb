THIS_MAKEFILE := $(realpath $(lastword $(MAKEFILE_LIST)))
PATH := ${SCRIPTS_DIR}:${PATH}
include gcp-env.sh
include env.sh
export

# FRAMEWORK SYNC
ifeq ($(MAKELEVEL),0)
    _ := $(shell >&2 echo)
    INSTALLED := $(firstword $(wildcard ${FRAMEWORK_DIR}))
    ifeq (${INSTALLED},)
		_ := $(shell >&2 echo Updating PS cloud framework in ${FRAMEWORK_DIR}...)
		_ := $(shell mkdir -p ${FRAMEWORK_DIR} && curl --retry 3 -s https://storage.googleapis.com/${FRAMEWORK_BUCKET}/stable/framework.tar.gz?random=$$(date +%s) | tar -xzf - -C ${FRAMEWORK_DIR})
		_ := $(shell >&2 echo - framework version: $$(cat ${FRAMEWORK_DIR}/sha.txt))
	endif
endif
# END FRAMEWORK SYNC

# COMMON MAKEFILE PARTS INCLUDES
include ${FRAMEWORK_DIR}/makefile_parts/shared.mk
include ${FRAMEWORK_DIR}/makefile_parts/config.mk
include ${FRAMEWORK_DIR}/makefile_parts/vault.mk
include ${FRAMEWORK_DIR}/makefile_parts/docker-compose.mk
include ${FRAMEWORK_DIR}/makefile_parts/sonarqube.mk
include ${FRAMEWORK_DIR}/makefile_parts/drone1.mk
include ${FRAMEWORK_DIR}/makefile_parts/deps.mk
# END COMMON MAKEFILE PARTS INCLUDES

.PHONY: build
build: build-docker ## build docker image

.PHONY: push
push: push-version push-latest ## push both latest and versioned image to docker registry

.PHONY: pull
pull: pull-latest ## pull latest image from project's docker registry

#.PHONY: scan
#scan: build-app sonar-scan ## mvn clean package and sonar-scan
#	@echo 'See report on https://sonarqube.med.stanford.edu/dashboard?id=lane:laneweb'

.PHONY: promote-last-git-tag
promote-last-git-tag: config-gcloud ## promote last git tag to prod-latest
	@git fetch --tags
	@export IMAGE_TAG=$$(git describe --abbrev=0 --tags); \
	export IMAGE_PATH="$${DOCKER_REGISTRY}/$${GCP_PROJECT_ID}/$${DOCKER_IMAGE}" ; \
	export DOCKER_IMAGE_VERSION=$${IMAGE_TAG} ;\
	echo "pulling $${DOCKER_IMAGE_VERSION}" ;\
	make pull-version ;\
	gcloud --quiet container images add-tag \
	$${IMAGE_PATH}:$${IMAGE_TAG} \
	$${IMAGE_PATH}:prod-latest ;\
	echo "promoted $${IMAGE_TAG} to prod-latest"
