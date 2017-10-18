FROM clojure:lein-2.7.1-alpine
MAINTAINER Maik Schwan <maik.schwan@gmail.com>

WORKDIR /cookbook

ADD project.clj .
RUN lein deps

EXPOSE 3000

ADD . .

RUN lein cljsbuild once production

ENTRYPOINT ["lein"]
CMD ["run"]
