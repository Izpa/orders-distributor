(ns orders-distributor.models
  (:require [toucan.models :as t]))

(t/defmodel TelegramUser :telegram_user)
(t/defmodel TelegramChat :telegram_chat)
(t/defmodel TelegramMessage :telegram_message
  t/IModel
  (hydration-keys [_]
    [:telegram_user_id :telegram_chat_id]))
