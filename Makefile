.PHONY: default dockerimage

default : build push

build:
	docker build --tag nesurion/cookbook .

run:
	docker run -P nesurion/cookbook:latest
