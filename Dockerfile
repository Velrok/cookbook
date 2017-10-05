FROM clojure:lein-2.7.1-alpine
MAINTAINER Maik Schwan <maik.schwan@gmail.com>


ADD project.clj .
RUN lein deps

EXPOSE 3000

ADD . .

RUN lein cljsbuild once production

ENTRYPOINT ["lein"]
CMD ["run"]
