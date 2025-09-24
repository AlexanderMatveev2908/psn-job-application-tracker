CREATE TABLE IF NOT EXISTS backup_codes (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    code VARCHAR(255) NOT NULL
) INHERITS (root_table);


ALTER TABLE backup_codes
    ADD CONSTRAINT backup_codes_pkey PRIMARY KEY (id);


CREATE INDEX idx_backup_codes_user_id ON backup_codes(user_id);


CREATE TRIGGER trigger_timestamps_backup_codes
BEFORE INSERT OR UPDATE ON backup_codes
FOR EACH ROW
EXECUTE FUNCTION set_timestamps();
