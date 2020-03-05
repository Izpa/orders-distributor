(ns orders-distributor.bots.common
  (:require [orders-distributor.models :as models]
            [toucan.db :as db]))

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
