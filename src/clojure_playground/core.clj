(ns clojure-playground.core
  (:require
    [clojure.string :as string :refer [split]]
    [ring.adapter.jetty :as jetty :refer [run-jetty]]
    [mount.core :as mount :refer [defstate]]
    [compojure.core :refer :all]
    [compojure.route :as route]))

(comment (defn handler [request]
           {:status  200
            :headers {"Content-Type" "text/html"}
            :body    "Hello World"}))

(defroutes app
           (GET "/" [] "<h1>Hello World</h1>")
           (route/not-found "<h1>Page not found</h1>"))

(defstate server :start (run-jetty app {:port  3000
                                        :join? false})
          :stop (.stop server))

(comment
  (mount/start)
  (mount/stop))