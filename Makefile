include env.sh

export VERSION=$(shell git rev-parse --verify HEAD)

mkfile_path := $(abspath $(lastword $(MAKEFILE_LIST)))
current_dir := $(patsubst %/,%,$(dir $(mkfile_path)))

all: app

app:
	docker run -v ${current_dir}:/build \
		maven bash -c "cd /build; mvn -s settings.xml clean package"

docker:
	docker build --build-arg PROJECT_VERSION=${PROJECT_VERSION} -t ${IMAGE}:${VERSION} .

docker_nc:
	docker build --no-cache=true --build-arg PROJECT_VERSION=${PROJECT_VERSION} -t ${IMAGE}:${VERSION} .

prune:
	docker rm `docker ps -q -a --filter status=exited`
	docker rmi `docker images -q --filter "dangling=true"`

clean_docker:
	docker ps -a | grep 'Exited' | awk '{print $1}' | xargs  docker rm

push:
	# docker registry login
	@docker login -e ${REGISTRY_EMAIL} -u ${REGISTRY_USER} -p '$(shell cat ${REGISTRY_KEY_FILE})' https://${REGISTRY}
	# push the version that derived from git commit sha
	docker tag ${IMAGE}:${VERSION} ${REGISTRY}/${PROJECT_ID}/${IMAGE}:${VERSION} 
	docker push ${REGISTRY}/${PROJECT_ID}/${IMAGE}:${VERSION}
	# push the version as the "latest"
	docker tag ${IMAGE}:${VERSION} ${REGISTRY}/${PROJECT_ID}/${IMAGE}:latest
	docker push ${REGISTRY}/${PROJECT_ID}/${IMAGE}:latest

pull:
	@docker login -e ${REGISTRY_EMAIL} -u ${REGISTRY_USER} -p '$(shell cat ${REGISTRY_KEY_FILE})' https://${REGISTRY}
	docker pull ${REGISTRY}/${PROJECT_ID}/${IMAGE}:latest

# for drone ci
sec: ${SCRIPTS_DIR}
	${SCRIPTS_DIR}/drone-secure.sh

${SCRIPTS_DIR}:
	git clone ${SCRIPTS_REPO} ${SCRIPTS_DIR} 

update-scripts: ${SCRIPTS_DIR}
	cd ${SCRIPTS_DIR}; git pull ${SCRIPTS_REPO}

help: 
	@echo make [ all | build | build_docker | build_nc | prune | push | sec ]

.PHONY: all build docker docker_nc prune push pull help sec update-scripts clean_docker