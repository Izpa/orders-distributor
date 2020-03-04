(ns orders-distributor.settings
  (:require [environ.core :refer [env]]))

(def telegram-token (env :telegram-token))
(def domain (env :domain))
(def redirect-telegram-id (env :redirect-telegram-id))

(def telegram-handler-uri "/telegram-handler")
