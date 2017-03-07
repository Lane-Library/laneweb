THIS_MAKEFILE := $(realpath $(lastword $(MAKEFILE_LIST)))
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

help: ## --
	@# adapted from https://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
	@echo '_________________'
	@echo '| Make targets: |'
	@echo '-----------------'
	@cat ${THIS_MAKEFILE} | grep -E '^[a-zA-Z_-]+:.*?## .*$$' | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: all build docker docker_nc prune push pull help sec update-scripts clean_docker
