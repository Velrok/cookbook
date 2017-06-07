(ns script.repl
  (:require
    [figwheel-sidecar.repl-api :refer [start-figwheel! cljs-repl]]))

(comment
  (start-figwheel!)
  (cljs-repl))