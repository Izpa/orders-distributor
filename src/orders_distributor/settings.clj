(ns orders-distributor.settings
  (:require [environ.core :refer [env]]))

(def acceptor-telegram-token (env :acceptor-telegram-token))
(def distributor-telegram-token (env :distributor-telegram-token))
(def domain (env :domain))
(def distributor-chat-telegram-id (env :distributor-chat-telegram-id))

(def base-telegram-handler-uri "/telegram-handler")
(def acceptor-telegram-handler-uri (str base-telegram-handler-uri "/acceptor"))
(def distributor-telegram-handler-uri (str base-telegram-handler-uri "/distributor"))
