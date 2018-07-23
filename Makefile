THIS_MAKEFILE := $(realpath $(lastword $(MAKEFILE_LIST)))
PATH := ${SCRIPTS_DIR}:${PATH}
include env.sh
export

# FRAMEWORK SYNC
ifeq ($(MAKELEVEL),0)
    _ := $(shell >&2 echo)
	ifneq ($(wildcard ${FRAMEWORK_DIR}/.git/),)
		_ := $(shell >&2 echo Updating PS cloud framework from Git into ${FRAMEWORK_DIR}...)
		_ := $(shell cd ${FRAMEWORK_DIR}; git pull)
	else
		_ := $(shell >&2 echo Updating PS cloud framework in ${FRAMEWORK_DIR}...)
		_ := $(shell mkdir -p ${FRAMEWORK_DIR} && curl --retry 3 -s https://storage.googleapis.com/${FRAMEWORK_BUCKET}/framework.tar.gz?random=$$(date +%s) | tar -xzf - -C ${FRAMEWORK_DIR})
		_ := $(shell >&2 echo - framework version: $$(cat ${FRAMEWORK_DIR}/sha.txt))
	endif
endif
# END FRAMEWORK SYNC

# COMMON MAKEFILE PARTS INCLUDES
include ${FRAMEWORK_DIR}/makefile_parts/shared.mk
include ${FRAMEWORK_DIR}/makefile_parts/vault.mk
include ${FRAMEWORK_DIR}/makefile_parts/docker-compose.mk
include ${FRAMEWORK_DIR}/makefile_parts/drone08.mk
include ${FRAMEWORK_DIR}/makefile_parts/deps.mk
# END COMMON MAKEFILE PARTS INCLUDES

.PHONY: build
build: build-app build-docker ## build app and docker image

build-app: ## build app
	docker run -v ${current_dir}:/build \
		maven bash -c "cd /build; mvn -B -q -s settings.xml clean package"

.PHONY: push
push: push-version push-latest ## push both latest and versioned image to docker registry

.PHONY: pull
pull: pull-latest ## pull latest image from project's docker registry