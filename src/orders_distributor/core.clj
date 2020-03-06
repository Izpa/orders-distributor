(ns orders-distributor.core
  (:require [compojure.handler :refer [site]]
            [environ.core :refer [env]]
            [heroku-database-url-to-jdbc.core :as h]
            [orders-distributor.bots.acceptor :as acceptor-bot]
            [orders-distributor.bots.distributor :as distributor-bot]
            [orders-distributor.web :as web]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as middleware]
            [toucan.db :as db]))

(def app
  (-> (site web/app)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

(defn -main [& [port]]
  (acceptor-bot/set-webhook)
  (distributor-bot/set-webhook)
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty app {:port port :join? false}))
  (-> :database-url
      env
      h/korma-connection-map
      db/set-default-db-connection!))
