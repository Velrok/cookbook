.PHONY: default dockerimage

default : build push

build:
	docker build --tag velrok/cookbook .

run:
	docker run -P velrok/cookbook:latest
