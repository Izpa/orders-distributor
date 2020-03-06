CREATE TABLE IF NOT EXISTS telegram_order (
  id bigserial PRIMARY KEY,
  create_message_id bigint REFERENCES telegram_message(id),
  accept_message_id bigint REFERENCES telegram_message(id),
  unaccept_message_id bigint REFERENCES telegram_message(id),
  cancel_message_id bigint REFERENCES telegram_message(id),
  complete_message_id bigint REFERENCES telegram_message(id),
  raiting_message_id bigint REFERENCES telegram_message(id),
  );
