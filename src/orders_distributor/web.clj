(ns orders-distributor.web
  (:require [compojure.core :refer [defroutes GET POST ANY]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [orders-distributor.bots.acceptor :as acceptor-bot]
            [orders-distributor.bots.distributor :as distributor-bot]
            [orders-distributor.settings :as s]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello"})

(defroutes app
  (GET "/" []
       (splash))
  (POST s/acceptor-handler-uri {body :body} (acceptor-bot/handler body))
  (POST s/distributor-handler-uri {body :body} (distributor-bot/handler body))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))
