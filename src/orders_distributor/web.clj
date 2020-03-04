(ns orders-distributor.web
  (:require [compojure.core :refer [defroutes GET POST ANY]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [orders-distributor.bot :as bot]
            [orders-distributor.settings :as s]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello"})

(defroutes app
  (GET "/" []
       (splash))
  (POST s/telegram-handler-uri {body :body} (bot/handler body))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))
