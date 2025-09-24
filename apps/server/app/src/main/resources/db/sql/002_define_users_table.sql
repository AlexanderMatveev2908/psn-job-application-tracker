CREATE TABLE IF NOT EXISTS users (
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tmp_email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    totp_secret VARCHAR(255)
) INHERITS (root_table);