(ns clojure-playground.app.main
  (:require [reagent.core :as r]))

(enable-console-print!)

(def current-title (r/atom "hello"))
(def recepies (r/atom #{{:title "Spagetti"} {:title "Beans on Toast"} {:title "Pizza"}}))

(defn header [s]
  [:h1 s])

(defn event-value [event]
  (.-value (.-target event)))

(defn new-recipe []
  (let [title (atom "")]
    [:div
     [:input {:type "text"
              :on-change (fn [event]
                           (reset! title (event-value event)))
              :placeholder "recipe title"}]
     [:button {:on-click (fn [event]
                           (swap! recepies conj {:title @title}))} "add"]]))

(defn hello []
  [:div
   [header "cookbook"]

   [new-recipe]
   [:ul
    (for [x @recepies]
      [:li (:title x)])
    ]])

(defn ^:export run []
  (r/render [hello]
            (js/document.getElementById "app")))