CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    telegram_id BIGINT NOT NULL,
    username VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_users_telegram_id ON users(telegram_id);