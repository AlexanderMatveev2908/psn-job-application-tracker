CREATE EXTENSION IF NOT EXISTS "pgcrypto";


CREATE TABLE IF NOT EXISTS root_table(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at BIGINT NOT NULL DEFAULT (extract(epoch FROM now()) * 1000)::BIGINT,
    updated_at BIGINT NOT NULL DEFAULT (extract(epoch FROM now()) * 1000)::BIGINT,
    deleted_at BIGINT
);

