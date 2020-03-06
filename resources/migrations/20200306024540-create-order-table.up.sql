CREATE TABLE IF NOT EXISTS telegram_order (
  id bigserial PRIMARY KEY,
  create_message_id bigint REFERENCES telegram_message(id),
  accept_user_id bigint REFERENCES telegram_user(id),
  unaccept_user_id bigint REFERENCES telegram_user(id),
  cancel_user_id bigint REFERENCES telegram_user(id),
  complete_user_id bigint REFERENCES telegram_user(id),
  raiting_message_id bigint REFERENCES telegram_message(id)
  );
