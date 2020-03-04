(ns orders-distributor.bot
  (:require [morse.handlers :as h]
            [morse.api :as api]
            [orders-distributor.settings :as s]))

(defn set-webhook []
  (let [webhook-url (str "https://" s/domain s/telegram-handler-uri)]
    (api/set-webhook s/telegram-token webhook-url)))

(h/defhandler handler
  (h/command "start" {{chat-id :id} :chat}
             (api/send-text s/telegram-token chat-id
                            (str "Приветствую! Здесь можно задать свой вопрос и на ближайшем митапе руководители ответят на него!)\n"
                                 "Два момента:\n"
                                 "1) Если хотите услышать ответ от кого-то конкретно, пожалуйста, укажите имя, кому адресуете свой вопрос\n"
                                 "2) Если хотите, чтобы ваш вопрос попал руководителям без указания вашего имени - напишите свой вопрос и далее фразу \"я хочу остаться инкогнито\"")))
  (h/command "help" {{chat-id :id} :chat}
             (api/send-text s/telegram-token chat-id
                            (str "Здесь можно задать свой вопрос и на ближайшем митапе руководители ответят на него!)\n"
                                 "Два момента:\n"
                                 "1) Если хотите услышать ответ от кого-то конкретно, пожалуйста, укажите имя, кому адресуете свой вопрос\n"
                                 "2) Если хотите, чтобы ваш вопрос попал руководителям без указания вашего имени - напишите свой вопрос и далее фразу \"я хочу остаться инкогнито\"")))
  (h/command "id" {{user-id :id} :from {chat-id :id} :chat}
             (api/send-text s/telegram-token chat-id user-id))
  (h/message {{first-name :first_name last-name :last_name user-name :username} :from
              text :text
              {chat-id :id} :chat}
             (when (and (some? text) (some? s/redirect-telegram-id))
               (api/send-text s/telegram-token s/redirect-telegram-id
                              (str first-name " " last-name " (" user-name ") спрашивает: " text))
               (api/send-text s/telegram-token chat-id "Ваш вопрос доставлен!"))))
