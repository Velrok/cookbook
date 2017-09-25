FROM openjdk:8
MAINTAINER Maik Schwan <maik.schwan@gmail.com>

ADD target/clojure-playground-0.1.0-SNAPSHOT-standalone.jar /usr/bin/app.jar
WORKDIR /usr/bin/
CMD ["java", "-jar", "app.jar"]

