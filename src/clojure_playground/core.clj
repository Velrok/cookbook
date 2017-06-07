(ns clojure-playground.core
  (:require
    [clojure.string :as string :refer [split]]
    [clojure.edn :as edn]
    [ring.adapter.jetty :as jetty :refer [run-jetty]]
    [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
    [mount.core :as mount :refer [defstate]]
    [compojure.core :refer [defroutes GET POST OPTIONS]]
    [compojure.route :as route]
    [cheshire.core :as json]
    [ring.util.response :as resp]))

(def recipes (atom (edn/read-string (slurp "server.edn"))))
(add-watch recipes :recipes-file-store
           (fn [_ _ _ new-value]
             (println "writing new recipe to file data store")
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
      ; @waldemar: how to avoid trying to encode files? (like index.html)
      (if (string? (:body response))
        response
        (update-in response [:body] json/encode)))))

(comment
  (spit "server.edn" (prn-str {:foo "bar"}))
  (edn/read-string (slurp "server.edn"))
  (deref recepies)
  (reset! recepies {:new "stuff"})
  (defn printer [x]
    (if (string? x)
      ((println x) (+ 2 3))
      (println "not a string")))
  (printer "t"))


(defn add-recipe [input]
  (println input)
  (swap! recipes (fn [current-recipes]
                   (assoc current-recipes (count current-recipes) input))))

(defroutes routes
           (GET "/recipes" []
             {:status 200
              :body   {:recipes (vals @recipes)}})
           (GET "/" [] (resp/resource-response "/index.html" {:root "public"}))
           (POST "/recipes" request (do
                                      (add-recipe (get request :body))
                                      {:status 200
                                       :body   {:type :okay}}))
           (route/resources "/")
           (route/not-found "<h1>Page not found</h1>"))

(def http-pipeline (-> routes
                       wrap-json-response
                       (wrap-json-body {:keywords? true})
                       access-controll-headers))

(defstate server :start (run-jetty #'http-pipeline {:port  3000
                                        :join? false})
          :stop (.stop server))

(defn restart []
  (mount/stop)
  (mount/start))

(comment
   (restart))