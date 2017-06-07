(ns clojure-playground.core
  (:require
    [clojure.string :as string :refer [split]]
    [clojure.edn :as edn]
    [ring.adapter.jetty :as jetty :refer [run-jetty]]
    [mount.core :as mount :refer [defstate]]
    [compojure.core :refer [defroutes GET POST OPTIONS]]
    [compojure.route :as route]
    [cheshire.core :as json]))

(def recepies (atom (edn/read-string (slurp "server.edn"))))
(add-watch recepies :recepies-file-store
           (fn [_ _ _ new-value]
             (println "writing new recepie to file data store")
             (spit "server.edn" (prn-str new-value))))

(defn access-controll-headers
  [handler]
  (fn [req]
    (let [response (handler req)]
      (update-in response
                 [:headers]
                 merge
                 {"Access-Control-Allow-Origin" "*"

                  "Access-Control-Allow-Headers"
                                                ["Content-Type"
                                                 "Authorization"
                                                 "Access-Control-Allow-Origin"]}))))

(defn response-encoder
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if (string? (:body response))
        response
        (update-in response [:body] json/encode)))))

(comment

  (spit "server.edn" (prn-str {:foo "bar"}))
  (edn/read-string (slurp "server.edn"))
  (deref recepies)
  (reset! recepies {:new "stuff"})
  )

(defroutes routes
           (GET "/recepies" []
             {:status 200
              :body   {:recepies (vals @recepies)}})
           (route/resources "/")
           (route/not-found "<h1>Page not found</h1>"))

(def http-pipeline (-> routes
                       response-encoder
                       access-controll-headers))

(defstate server :start (run-jetty #'http-pipeline {:port  3000
                                        :join? false})
          :stop (.stop server))

(defn restart []
  (mount/stop)
  (mount/start))

(comment
   (restart))