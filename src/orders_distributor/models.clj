(ns orders-distributor.models
  (:require [toucan.models :as t]))

(t/defmodel TelegramUser :telegram_user_id)
(t/defmodel TelegramChat :telegram_chat_id)
(t/defmodel TelegramMessage :telegram_message)
