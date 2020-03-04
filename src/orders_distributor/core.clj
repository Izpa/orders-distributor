(ns orders-distributor.core
  (:require [compojure.handler :refer [site]]
            [environ.core :refer [env]]
            [orders-distributor.bot :as bot]
            [orders-distributor.web :as web]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as middleware]
            [toucan.db :as db]))

(def app
  (-> (site web/app)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

(defn -main [& [port]]
  (bot/set-webhook)
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty app {:port port :join? false}))
  (db/set-default-db-connection! (env :database_url)))
