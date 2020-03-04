CREATE TABLE IF NOT EXISTS telegram_user (
  id bigserial PRIMARY KEY,
  external_id bigint UNIQUE NOT NULL,
  first_name varchar(100),
  last_name varchar(100),
  username varchar(100),
  is_bot boolean NOT NULL,
  language_code varchar(100)
);
