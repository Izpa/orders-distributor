(ns orders-distributor.models
  (:require [toucan.models :as t]))

(t/defmodel TelegramUser :telegram_user
  t/IModel
  (hydration-keys [_]
    [:telegram_user_id]))
(t/defmodel TelegramChat :telegram_chat
  t/IModel
  (hydration-keys [_]
    [:telegram_chat_id]))
(t/defmodel TelegramMessage :telegram_message)
