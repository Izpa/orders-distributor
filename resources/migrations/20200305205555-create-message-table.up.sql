CREATE TABLE IF NOT EXISTS telegram_message (
  id bigserial PRIMARY KEY,
  external_id bigint UNIQUE NOT NULL,
  text TEXT,
  telegram_user_id bigint REFERENCES telegram_user(id),
  telegram_chat_id bigint REFERENCES telegram_chat(id),
  telegram_date INTEGER);
