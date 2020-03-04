(ns orders-distributor.bot
  (:require [morse.handlers :as h]
            [morse.api :as api]
            [orders-distributor.settings :as s]))

(defn set-webhook []
  (let [webhook-url (str "https://" s/domain s/telegram-handler-uri)]
    (api/set-webhook s/telegram-token webhook-url)))

(h/defhandler handler
  (h/command "test" {{chat-id :id} :chat :as msg}
             (api/send-text s/telegram-token chat-id msg))
  (h/command "id" {{user-id :id} :from {chat-id :id} :chat}
             (api/send-text s/telegram-token chat-id user-id))
  (h/message {{chat-id :id} :chat :as msg}
             (api/send-text s/telegram-token chat-id msg)))
