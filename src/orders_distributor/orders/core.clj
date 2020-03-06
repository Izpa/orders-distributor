(ns orders-distributor.orders.core
  (:require [clojure.string :as str]
            [morse.api :as api]
            [orders-distributor.bots.common :as t]
            [orders-distributor.orders.db :as db]
            [orders-distributor.settings :as s]))

(defn- remove-command [text]
  (let [clear-text-vector (-> text
                              (str/split #" ")
                              (subvec 1))]
    (str/join " " clear-text-vector)))

(defn- user-string [first-name last-name username]
  (str first-name " " last-name " (" username ")"))

(defn- command->str [command & args]
  (str (name command)
       " "
       (str/join " " args)))

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
                                 order-text)
        callback-data (command->str :accept order-id)
        options {:reply_markup {:inline_keyboard [[{:text "Принять"
                                                    :callback_data callback-data}]]}}]
    (api/send-text s/distributor-telegram-token
                   s/distributor-chat-telegram-id
                   options
                   order-to-distribute)
    (api/send-text s/acceptor-telegram-token
                   chat-id
                   "Ищем исполнителя для вашего заказа, пожалуйста подождите...")))

(defn accept [order-id user-external-id callback-external-id message-data]
  (let [user (-> user-external-id
                 t/external-id->telegram-user)
        {:keys [id first_name last_name username]} user
        order (db/id->order order-id)
        {:keys [chat-id message-id text]} message-data
        new-text (str text "\n Начал выполнять " (user-string first_name last_name username))]
    (if user
      (api/edit-text s/distributor-telegram-token chat-id message-id new-text)
      (api/answer-callback s/distributor-telegram-token
                           callback-external-id
                           (str "Вы не можете принимать заказы, так как не написали боту в личку")
                           true))))

(defn route-callback [callback]
  (let [{:keys [id data from message]} callback
        user-external-id (:id from)
        message-data {:chat-id (-> message
                                   :from
                                   :chat
                                   :id)
                      :message-id (:message_id message)
                      :text (:text message)}
        splitted-data (str/split data #" ")
        command (-> splitted-data
                    first
                    keyword)
        args (rest splitted-data)]
    (case command
      :accept (accept (->> args
                          first
                           #(Integer. (re-find  #"\d+" %)))
                      user-external-id
                      id
                      message-data))))
