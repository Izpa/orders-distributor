(ns orders-distributor.bots.distributor
  (:require [morse.handlers :as h]
            [morse.api :as api]
            [orders-distributor.bots.common :as b]
            [orders-distributor.settings :as s]))

(defn set-webhook []
  (let [webhook-url (str "https://" s/domain s/distributor-telegram-handler-uri)]
    (api/set-webhook s/distributor-telegram-token webhook-url)))

(h/defhandler handler
  (h/command "test" {{chat-id :id} :chat :as msg}
             (api/send-text s/distributor-telegram-token chat-id msg))
  (h/command "external-id" {{external-id :id} :from {chat-id :id} :chat}
             (api/send-text s/distributor-telegram-token chat-id external-id))
  (h/command "id" {{external-id :id
                    first-name :first_name
                    last-name :last_name
                    username :username
                    is-bot :is_bot
                    language-code :language_code} :from
                   {chat-id :id} :chat}
             (api/send-text s/distributor-telegram-token chat-id
                            (b/get-or-create-telegram-user-id! external-id
                                                               first-name
                                                               last-name
                                                               username
                                                               is-bot
                                                               language-code)))
  (h/message {{chat-id :id} :chat :as msg}
             (api/send-text s/distributor-telegram-token chat-id msg)))
