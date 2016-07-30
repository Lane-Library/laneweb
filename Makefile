all: build_docker

build:
	mvn -s settings.xml clean package
	mvn -s settings.xml org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate \
		-Dexpression=project.version \
		| grep -Ev '(^\[|Download\w+:)' 2> /dev/null > build/project-version

build_docker: #build
	docker build --build-arg PROJECT_VERSION=$(shell cat target/project-version) -t ${IMAGE}:${VERSION} .

build_nc: build
	docker build --no-cache=true -t ${IMAGE}:${VERSION} .

prune:
	docker rm `docker ps -q -a --filter status=exited`
	docker rmi `docker images -q --filter "dangling=true"`

push:
	@docker login -e ${REGISTRY_EMAIL} -u ${REGISTRY_USER} -p '$(shell cat ${REGISTRY_KEY_FILE})' https://${REGISTRY}
	docker tag ${IMAGE}:${VERSION} ${REGISTRY}/${PROJECT_ID}/${IMAGE}:${VERSION} 
	docker push ${REGISTRY}/${PROJECT_ID}/${IMAGE}:${VERSION}
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

.PHONY: all build build_docker build_nc prune push pull help sec update-scripts