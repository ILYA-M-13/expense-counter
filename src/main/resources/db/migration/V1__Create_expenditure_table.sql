CREATE TABLE expenditure (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    expend DOUBLE PRECISION NOT NULL,
    category VARCHAR(255),
    datetime TIMESTAMP NOT NULL
);

CREATE INDEX idx_expenditure_user_datetime ON expenditure (user_id, datetime);