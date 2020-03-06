(ns orders-distributor.bots.common
  (:require [orders-distributor.models :as models]
            [toucan.db :as db]
            [toucan.hydrate :refer [hydrate]]))

(defn add-telegram-user! [external-id first-name last-name username is-bot language-code]
  (db/insert! models/TelegramUser {:external_id external-id
                                   :first_name first-name
                                   :last_name last-name
                                   :username username
                                   :is_bot is-bot
                                   :language_code language-code}))

(defn external-id->telegram-user [external-id]
  (first (db/select models/TelegramUser :external_id external-id)))

(defn get-or-create-telegram-user!
  [external-id first-name last-name username is-bot language-code]
  (let [exist-telegram-user (external-id->telegram-user external-id)]
    (if (some? exist-telegram-user)
      exist-telegram-user
      (do (add-telegram-user! external-id first-name last-name username is-bot language-code)
          (external-id->telegram-user external-id)))))

(defn add-telegram-chat!
  [external-id first-name last-name username title chat-type all-members-are-administrators]
  (db/insert! models/TelegramChat {:external_id external-id
                                   :first_name first-name
                                   :last_name last-name
                                   :username username
                                   :title title
                                   :chat_type chat-type
                                   :all_members_are_administrators all-members-are-administrators}))

(defn external-id->telegram-chat [external-id]
  (first (db/select models/TelegramChat :external_id external-id)))

(defn get-or-create-telegram-chat!
  [external-id first-name last-name username title chat-type all-members-are-administrators]
  (let [exist-telegram-chat (external-id->telegram-chat external-id)]
    (if (some? exist-telegram-chat)
      exist-telegram-chat
      (do (add-telegram-chat! external-id
                              first-name
                              last-name
                              username
                              title
                              chat-type
                              all-members-are-administrators)
          (external-id->telegram-chat external-id)))))

(defn add-telegram-message!
  [external-id text telegram-user-id telegram-chat-id telegram-date]
  (db/insert! models/TelegramMessage {:external_id external-id
                                      :text text
                                      :telegram_user_id telegram-user-id
                                      :telegram_chat_id telegram-chat-id
                                      :telegram_date telegram-date}))

(defn external-id->telegram-message [external-id]
  (hydrate (first (db/select models/TelegramMessage :external_id external-id))
              :telegram_user_id
              :telegram_chat_id))

(defn get-or-create-telegram-message!
  [external-id text telegram-user-id telegram-chat-id telegram-date]
  (let [exist-telegram-message (external-id->telegram-message external-id)]
    (if (some? exist-telegram-message)
      exist-telegram-message
      (do (add-telegram-message! external-id text telegram-user-id telegram-chat-id telegram-date)
          (external-id->telegram-message external-id)))))

(defn incoming-message! [{message-external-id :message_id
                          {user-external-id :id
                           user-first-name :first_name
                           user-last-name :last_name
                           user-username :username
                           user-is-bot :is_bot
                           user-language-code :language_code} :from
                          {chat-external-id :id
                           chat-first-name :first_name
                           chat-last-name :last_name
                           chat-username :username
                           chat-title :title
                           chat-type :type
                           all-members-are-administrators :all_members_are_administrators} :chat
                          text :text
                          telegram-date :date}]
  (let [telegram-user (get-or-create-telegram-user! user-external-id
                                                    user-first-name
                                                    user-last-name
                                                    user-username
                                                    user-is-bot
                                                    user-language-code)
        telegram-chat (get-or-create-telegram-chat! chat-external-id
                                                    chat-first-name
                                                    chat-last-name
                                                    chat-username
                                                    chat-title
                                                    chat-type
                                                    all-members-are-administrators)]
    (get-or-create-telegram-message! message-external-id
                                     text
                                     (:id telegram-user)
                                     (:id telegram-chat)
                                     telegram-date)))
