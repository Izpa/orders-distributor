(ns orders-distributor.bots.acceptor
  (:require [morse.handlers :as h]
            [morse.api :as api]
            [orders-distributor.orders :as orders]
            [orders-distributor.bots.common :as b]
            [orders-distributor.settings :as s]))

(defn set-webhook []
  (let [webhook-url (str "https://" s/domain s/acceptor-telegram-handler-uri)]
    (api/set-webhook s/acceptor-telegram-token webhook-url)))

(h/defhandler handler
  (h/command "start" {{chat-id :id} :chat}
             (api/send-text s/acceptor-telegram-token chat-id
                            "Чего изволите?"))
  (h/command "debug" {{chat-id :id} :chat :as msg}
             (api/send-text s/acceptor-telegram-token chat-id
                            (b/incoming-message! msg)))
  (h/message-fn (fn [msg] (-> msg
                              b/incoming-message!
                              orders/new))))
