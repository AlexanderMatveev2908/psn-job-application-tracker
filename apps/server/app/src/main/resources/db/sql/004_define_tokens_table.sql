DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'token_t') THEN
        CREATE TYPE token_t AS ENUM (
            'REFRESH',
            'CONF_EMAIL',
            'RECOVER_PWD',
            'RECOVER_PWD_2FA',
            'CHANGE_EMAIL',
            'CHANGE_EMAIL_2FA',
            'CHANGE_PWD',
            'MANAGE_ACC',
            'LOGIN_2FA',
            'MANAGE_ACC_2FA'
        );
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'alg_t') THEN
        CREATE TYPE alg_t AS ENUM (
        'AES_CBC_HMAC_SHA256',
        'RSA_OAEP256_A256GCM',
        'HMAC_SHA256'
        );
    END IF;
END$$;


CREATE TABLE IF NOT EXISTS tokens (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_type token_t NOT NULL,
    alg_type alg_t NOT NULL,
    hashed BYTEA NOT NULL,
    exp BIGINT NOT NULL
) INHERITS (root_table);


ALTER TABLE tokens
    ADD CONSTRAINT tokens_pkey PRIMARY KEY (id);


CREATE INDEX idx_tokens_user_id ON tokens(user_id);


CREATE TRIGGER trigger_timestamps_tokens
BEFORE INSERT OR UPDATE ON tokens
FOR EACH ROW
EXECUTE FUNCTION set_timestamps();
