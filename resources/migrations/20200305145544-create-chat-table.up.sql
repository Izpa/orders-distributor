CREATE TABLE IF NOT EXISTS telegram_chat (
  id bigserial PRIMARY KEY,
  external_id bigint UNIQUE NOT NULL,
  first_name varchar(100),
  last_name varchar(100),
  username varchar(100),
  title varchar(100),
  chat_type varchar(100),
  all_members_are_administrators boolean);
