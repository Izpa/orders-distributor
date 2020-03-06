(ns orders-distributor.orders
  (:require [clojure.string :as str]
            [morse.api :as api]
            [orders-distributor.bots.common :as b]
            [orders-distributor.settings :as s]))

(defn new [msg]
  (let [chat-id (get-in msg [:telegram_chat :external_id])
        {{:keys [first_name last_name username]} :telegram_user} msg
        order-text (-> msg
                       :text)
        order-to-distribute (->> order-text
                                 (str/join " ")
                                 (str "Новый заказ\n"
                                      "От " first_name " " last_name " (" username ")\n"))]
    (api/send-text s/distributor-telegram-token
                   s/distributor-chat-telegram-id
                   order-to-distribute)
    (api/send-text s/acceptor-telegram-token
                   chat-id
                   "Заказ выполняется...")))
