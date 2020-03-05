(ns orders-distributor.core
  (:require [compojure.handler :refer [site]]
            [environ.core :refer [env]]
            [heroku-database-url-to-jdbc.core :as h]
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
  (clojure.pprint/pprint "azaza")
  (clojure.pprint/pprint (env :database_url))
  (clojure.pprint/pprint (h/korma-connection-map (env :database_url)))
  (-> :database_url
      env
      h/korma-connection-map
      db/set-default-db-connection!))
