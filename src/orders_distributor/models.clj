(ns orders-distributor.models
  (:require [toucan.models :as t]))

(t/defmodel TelegramUser :telegram_user
  t/IModel
  (hydration-keys [_]
    [:telegram_user
     :accept_user
     :unaccept_user
     :cancel_user
     :complete_user]))
(t/defmodel TelegramChat :telegram_chat
  t/IModel
  (hydration-keys [_]
    [:telegram_chat]))
(t/defmodel TelegramMessage :telegram_message
  t/IModel
  (hydration-keys [_]
    [:create_message
     :raiting_message]))
(t/defmodel TelegramOrder :telegram_order)
