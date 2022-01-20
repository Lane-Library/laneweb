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
include ${FRAMEWORK_DIR}/makefile_parts/gitlab.mk
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

