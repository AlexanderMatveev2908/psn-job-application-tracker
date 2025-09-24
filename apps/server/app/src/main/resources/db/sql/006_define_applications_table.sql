DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'application_status_t') THEN
        CREATE TYPE application_status_t AS ENUM (
            'APPLIED',
            'UNDER_REVIEW',
            'INTERVIEW',
            'OFFER',
            'REJECTED',
            'WITHDRAWN'
        );
    END IF;
END$$;


CREATE OR REPLACE FUNCTION set_applied_at()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.applied_at IS NULL THEN
    NEW.applied_at := (extract(epoch FROM now()) * 1000)::BIGINT;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TABLE IF NOT EXISTS applications (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    company_name VARCHAR(255) NOT NULL,
    position_name VARCHAR(255) NOT NULL,
    status application_status_t NOT NULL,
    applied_at BIGINT DEFAULT (extract(epoch FROM now()) * 1000)::BIGINT

) INHERITS (root_table);


ALTER TABLE applications
    ADD CONSTRAINT applications_pkey PRIMARY KEY (id);


CREATE INDEX idx_applications_user_id ON applications(user_id);


CREATE TRIGGER trigger_set_applied_at
BEFORE INSERT ON applications
FOR EACH ROW
EXECUTE FUNCTION set_applied_at();


CREATE TRIGGER trigger_timestamps_applications
BEFORE INSERT OR UPDATE ON applications
FOR EACH ROW
EXECUTE FUNCTION set_timestamps();
