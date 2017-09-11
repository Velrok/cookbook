(ns clojure-playground.app.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs.core.async :refer [<!]]
            [clojure-playground.app.io :as io]))

(enable-console-print!)

(def current-title (r/atom "hello"))

(defn header [s]
  [:div.row.mt-2.mb-3
   [:div.col
    [:h1 s]]])

(defn event-value [event]
  (.-value (.-target event)))


(defn bootstrap-input [title html-attr]
  (let [id (gensym (str title "-"))]
    [:div.form-group
     [:label {:for id} title]
     [:input.form-control (assoc html-attr :id id)]])
  )


(defn bootstrap-textarea [title html-attr]
  (let [id (gensym (str title "-"))]
    [:div.form-group
     [:label {:for id} title]
     [:textarea.form-control (assoc html-attr :id id)]])
  )

(defn button
  ([label on-click-fn]
   (button label on-click-fn "btn-default"))
  ([label on-click-fn class]
   [:button.btn
    {:class    class
     :type     "button"
     :on-click on-click-fn}
    label]))


(defn new-recipe []
  (let [title (atom "")
        description (atom "")]
    [:div.row
     [:div.col
      [:div.card
       [:div.card-block
        [:h4.card-title "Add recipe"]
        [:form
         [bootstrap-input "Title"
          {:type        "text"
           :placeholder "Enter title"
           :on-change   (fn [event]
                          (reset! title (event-value event)))}]
         [bootstrap-textarea "Description"
          {:type        "text"
           :placeholder "Enter description"
           :on-change   (fn [event]
                          (reset! description (event-value event)))}]
         [button "add" (fn [event]
                         (go (<! (io/store-recipe {:title       @title
                                                   :description @description}))
                             (io/reset-recipes))
                         false)
          "btn-primary"]]]]]]))


(defn show-recipe-in-accordion [recipe accordion-id]
  (let [card-id (gensym "card")]
    [:div.card
     [:div.card-header {:role "tab"}
      [:h5.mb-0
       [:a {:data-toggle "collapse"
            :data-parent (str "#" accordion-id)
            :href        (str "#" card-id)}
        (str (:title recipe))]]]
     [:div.collapse {:role "tabpanel"
                     :id   card-id}
      (if (= (:ui-state recipe) :edit)
        (let [description-edit (atom (:description recipe))]
          [:div.card-block
           [:textarea.form-control
            {:on-change (fn [event]
                          (reset! description-edit (event-value event)))}
            (:description recipe)]
           [button "cancel"
            (fn [event]
              (swap! io/recipes
                     (fn [recipes]
                       (assoc-in recipes
                                 [(keyword (:id recipe)) :ui-state]
                                 :show))))]
           [button "save" (fn [event]
                            (go (<! (io/store-recipe {:id          (:id recipe)
                                                      :description @description-edit}))
                                (io/reset-recipes))
                            false)
            "btn-primary"]])
        [:div.card-block
         {:on-click (fn [event]
                      (swap! io/recipes
                             (fn [recipes]
                               (assoc-in recipes
                                         [(keyword (:id recipe)) :ui-state]
                                         :edit))))}
         (:description recipe)])]]))


(defn recipes-accordion [recipes]
  [:div.row.mb-4
   [:div.col
    (let [id (gensym "accordion-")]
      [:div {:role "tablist"
             :id   id}
       (for [recipe recipes]
         [show-recipe-in-accordion recipe id])])]])

(defn debug [label state-atom]
  [:div
   [:h6 label]
   [:code
    (prn-str state-atom)]])
;; As a user I want to edit a recipe that I've added previously
(defn hello []
  [:div
   [header "cookbook"]
   [recipes-accordion (sort-by :title (vals @io/recipes))]
   [new-recipe]])


(defn ^:export run []
  (io/reset-recipes)
  (r/render [hello]
            (js/document.getElementById "app")))