(ns orders-distributor.core
  (:require [compojure.handler :refer [site]]
            [ring.middleware.json :as middleware]
            [environ.core :refer [env]]
            [ring.adapter.jetty :as jetty]
            [orders-distributor.bot :as bot]
            [orders-distributor.web :as web]
            [orders-distributor.settings :as s]))

(def app
  (-> (site web/app)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

(defn -main [& [port]]
  (bot/set-webhook)
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty app {:port port :join? false})))
