(ns orders-distributor.orders.core
  (:require [clojure.string :as str]
            [morse.api :as api]
            [orders-distributor.orders.db :as db]
            [orders-distributor.settings :as s]))

(defn- remove-command [text]
  (let [clear-text-vector (-> text
                              (str/split #" ")
                              (subvec 1))]
    (str/join " " clear-text-vector)))

(defn- user-string [first-name last-name username]
  (str first-name " " last-name " (" username ")"))

(defn new [msg]
  (let [chat-id (get-in msg [:telegram_chat :external_id])
        {{:keys [first_name last_name username]} :telegram_user} msg
        order-id (db/create-order! (:id msg))
        order-text (-> msg
                       :text
                       remove-command)
        from-string (user-string first_name last_name username)
        order-to-distribute (str "Новый заказ #" order-id "\n"
                                 "От " from-string "\n"
                                 order-text)]
    (api/send-text s/distributor-telegram-token
                   s/distributor-chat-telegram-id
                   order-to-distribute)
    (api/send-text s/acceptor-telegram-token
                   chat-id
                   "Ищем исполнителя для вашего заказа, пожалуйста подождите...")))
