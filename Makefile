IMAGE_NAME:=leosdev/discography-service
IMAGE_TAG:=latest

SONARQUBE_CONTAINER_NAME:=sonarqube

default:
	cat ./Makefile

dist:
	mvn clean package -DskipTests

test:
	mvn verify sonar:sonar

image:
	docker build -t $(IMAGE_NAME):$(IMAGE_TAG) .

run-sonarqube:
	docker run -d --name $(SONARQUBE_CONTAINER_NAME) -p 9000:9000 -e SONAR_FORCEAUTHENTICATION=false sonarqube:latest

run-docker-compose:
	docker-compose up -d

run: dist image run-docker-compose

run-sonar: run-sonarqube

stop:
	docker-compose down  || true
	docker stop $(SONARQUBE_CONTAINER_NAME)  || true
	docker rm $(SONARQUBE_CONTAINER_NAME)  || true

