.PHONY: default dockerimage

default : build push

push:
	docker push nesurion/cookbook:latest

build:
	docker build --tag nesurion/cookbook .

run:
	docker run -P nesurion/cookbook:latest
