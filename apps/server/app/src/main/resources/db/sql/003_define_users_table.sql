CREATE TABLE IF NOT EXISTS users (
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tmp_email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    totp_secret VARCHAR(255)
) INHERITS (root_table);


ALTER TABLE users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


CREATE TRIGGER trigger_timestamps_users
BEFORE INSERT OR UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION set_timestamps();
