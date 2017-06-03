THIS_MAKEFILE := $(realpath $(lastword $(MAKEFILE_LIST)))
SHELL := /bin/bash
include env.sh

export VERSION=$(shell git rev-parse --verify HEAD)

mkfile_path := $(abspath $(lastword $(MAKEFILE_LIST)))
current_dir := $(patsubst %/,%,$(dir $(mkfile_path)))

all: help

app: ## --
	docker run -v ${current_dir}:/build \
		maven bash -c "cd /build; mvn -s settings.xml clean package"

docker: ## --
	docker build -t ${IMAGE}:${VERSION} .

docker_nc: ## --
	docker build --no-cache=true -t ${IMAGE}:${VERSION} .

prune: ## --
	docker rm `docker ps -q -a --filter status=exited`
	docker rmi `docker images -q --filter "dangling=true"`

clean_docker: ## --
	docker ps -a | grep 'Exited' | awk '{print $1}' | xargs  docker rm

push: ## --
	# docker registry login
	@docker login -e ${REGISTRY_EMAIL} -u ${REGISTRY_USER} -p '$(shell cat ${REGISTRY_KEY_FILE})' https://${REGISTRY}
	# push the version that derived from git commit sha
	docker tag ${IMAGE}:${VERSION} ${REGISTRY}/${PROJECT_ID}/${IMAGE}:${VERSION} 
	docker push ${REGISTRY}/${PROJECT_ID}/${IMAGE}:${VERSION}
	# push the version as the "latest"
	docker tag ${IMAGE}:${VERSION} ${REGISTRY}/${PROJECT_ID}/${IMAGE}:latest
	docker push ${REGISTRY}/${PROJECT_ID}/${IMAGE}:latest

pull: ## --
	@docker login -e ${REGISTRY_EMAIL} -u ${REGISTRY_USER} -p '$(shell cat ${REGISTRY_KEY_FILE})' https://${REGISTRY}
	docker pull ${REGISTRY}/${PROJECT_ID}/${IMAGE}:latest

# for drone ci
sec: ${SCRIPTS_DIR} ## --
	${SCRIPTS_DIR}/drone-secure.sh

${SCRIPTS_DIR}: ## --
	git clone ${SCRIPTS_REPO} ${SCRIPTS_DIR} 

update-scripts: ${SCRIPTS_DIR} ## --
	cd ${SCRIPTS_DIR}; git pull ${SCRIPTS_REPO}

# SHARED MAKEFILE TARGETS
help: ## show this help page
	@# adapted from https://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
	@echo '_________________'
	@echo '| Make targets: |'
	@echo '-----------------'
	@cat ${THIS_MAKEFILE} | grep -E '^[a-zA-Z_-]+:.*?## .*$$' | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

gen-phony: ## automatically generate .PHONY targets
	@echo 'Execute the following line:'
	@echo 'bash replace_phony.sh && rm replace_phony.sh'
	@cat ${THIS_MAKEFILE} | perl -ne 'print if s/^([a-zA-Z_-]+):.*/\1/' | fmt -w 120 | (echo; while IFS=$$'\n' read -r line; do echo ".PHONY: $$line"; done) > PHONY.tmp && \
		echo "sed -i '' '/^.PHONY:.*/d' ${THIS_MAKEFILE} && sed -i '' -e :a -e '/^\n*$$/{$$d;N;};/\n$$/ba' ${THIS_MAKEFILE} && cat PHONY.tmp >> ${THIS_MAKEFILE} && rm PHONY.tmp && echo Changes: && git diff ${THIS_MAKEFILE}" > replace_phony.sh && \
		chmod +x replace_phony.sh
# END SHARED MAKEFILE TARGETS

.PHONY: all build docker docker_nc prune push pull help sec update-scripts clean_docker
