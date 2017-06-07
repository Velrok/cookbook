(defproject clojure-playground "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.6.0-RC3"]
                 [mount "0.1.11"]
                 [compojure "1.5.2"]
                 [org.clojure/core.async "0.3.442"]
                 [cheshire "5.7.0"]
                 [com.cognitect/transit-clj "0.8.300"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [cljs-http "0.1.42"]
                 [org.clojure/clojurescript "1.9.521"]
                 [reagent "0.6.1"]
                 [secretary "1.2.3"]
                 [figwheel-sidecar "0.5.0"]]
  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-figwheel "0.5.10"]]
  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this

             ;; doesn't work for you just run your own server 🙂 (see lein-ring)

             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you are using emacsclient you can just use
             ;; :open-file-command "emacsclient"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             }
  :main clojure-playground.core
  :cljsbuild {:builds
              [{:id :production
                :source-paths ["src"]
                :compiler {:main  "clojure-playground.app.main"
                           :asset-path "js/out/prod"
                           :output-to "resources/public/js/out/prod/main.min.js"
                           :output-dir "resources/public/js/out/prod"
                           :optimizations :advanced
                           :pretty-print false}}

               {:id :figwheeel
                :source-paths ["src"]
                :figwheel {:on-jsload "clojure-playground.app.main/run"}
                :compiler {:main "clojure-playground.app.main"
                           :asset-path "js/out/dev"
                           :output-to "resources/public/js/out/dev/main.js"
                           :output-dir "resources/public/js/out/dev"
                           :optimizations :none
                           :pretty-print true
                           }}]}
)
