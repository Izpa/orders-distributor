(ns orders-distributor.bots.acceptor
  (:require [morse.handlers :as h]
            [morse.api :as api]
            [orders-distributor.models :as models]
            [orders-distributor.settings :as s]
            [toucan.db :as db]))

(defn set-webhook []
  (let [webhook-url (str "https://" s/domain s/acceptor-telegram-handler-uri)]
    (api/set-webhook s/acceptor-telegram-token webhook-url)))

(defn add-telegram-user! [external-id first-name last-name username is-bot language-code]
  (db/insert! models/TelegramUser {:external_id external-id
                                   :first_name first-name
                                   :last_name last-name
                                   :username username
                                   :is_bot is-bot
                                   :language_code language-code}))

(defn external-id->telegram-user [external-id]
  (db/select-one models/TelegramUser :external_id external-id))

(defn get-or-create-telegram-user-id!
  [external-id first-name last-name username is-bot language-code]
  (let [exist-telegram-user (external-id->telegram-user external-id)]
    (if (some? exist-telegram-user)
      (:id exist-telegram-user)
      (-> (add-telegram-user! external-id first-name last-name username is-bot language-code)
          (external-id->telegram-user external-id)
          :id))))

(h/defhandler handler
  (h/command "test" {{chat-id :id} :chat :as msg}
             (api/send-text s/acceptor-telegram-token chat-id msg))
  (h/command "external-id" {{external-id :id} :from {chat-id :id} :chat}
             (api/send-text s/acceptor-telegram-token chat-id external-id))
  (h/command "id" {{external-id :id
                    first-name :first_name
                    last-name :last_name
                    username :username
                    is-bot :is_bot
                    language-code :language_code} :from
                   {chat-id :id} :chat}
             (api/send-text s/acceptor-telegram-token chat-id
                            (get-or-create-telegram-user-id! external-id
                                                             first-name
                                                             last-name
                                                             username
                                                             is-bot
                                                             language-code)))
  (h/message {{chat-id :id} :chat :as msg}
             (api/send-text s/acceptor-telegram-token chat-id msg)))
