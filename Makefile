THIS_MAKEFILE := $(realpath $(lastword $(MAKEFILE_LIST)))
PATH := ${SCRIPTS_DIR}:${PATH}
include env.sh
export

ifneq ($(wildcard ${SCRIPTS_DIR}/.*),)
	_ := $(shell cd ${SCRIPTS_DIR}; git pull )
else
	_ := $(shell git clone ${SCRIPTS_REPO} ${SCRIPTS_DIR})
endif

# COMMON MAKEFILE PARTS INCLUDES
include ${SCRIPTS_DIR}/makefile_parts/shared.mk
include ${SCRIPTS_DIR}/makefile_parts/vault.mk
include ${SCRIPTS_DIR}/makefile_parts/docker-compose.mk
include ${SCRIPTS_DIR}/makefile_parts/drone08.mk
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