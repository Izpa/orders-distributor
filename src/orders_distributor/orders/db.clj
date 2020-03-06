(ns orders-distributor.orders.db
  (:require [orders-distributor.models :as models]
            [toucan.db :as db]
            [toucan.hydrate :refer [hydrate]]))

(defn create-order! [create-message-id]
  (db/simple-insert! models/TelegramOrder {:create_message_id create-message-id}))

(defn id->order [id]
  (hydrate (models/TelegramOrder id)
           [:create_message :telegram_user :telegram_chat]
           :accept_user
           :unaccept_user
           :cancel_user
           :complete_user))

(defn accept-order! [order-id user-id]
  (db/update! models/TelegramOrder order-id :accept_user user-id))

(defn unaccept-order! [order-id user-id]
  (db/update! models/TelegramOrder order-id :unaccept_user user-id))

(defn cancel-order! [order-id user-id]
  (db/update! models/TelegramOrder order-id :cancel_user user-id))

(defn complete-order! [order-id user-id]
  (db/update! models/TelegramOrder order-id :complete_user user-id))

(defn raiting-order! [order-id message-id]
  (db/update! models/TelegramOrder order-id :raiting_message message-id))
