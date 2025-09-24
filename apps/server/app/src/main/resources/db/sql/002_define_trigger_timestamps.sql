CREATE OR REPLACE FUNCTION set_timestamps()
RETURNS TRIGGER AS $$
BEGIN
  IF TG_OP = 'INSERT' THEN
    NEW.created_at := (extract(epoch FROM now()) * 1000)::BIGINT;
    NEW.updated_at := (extract(epoch FROM now()) * 1000)::BIGINT;
  ELSIF TG_OP = 'UPDATE' THEN
    NEW.updated_at := (extract(epoch FROM now()) * 1000)::BIGINT;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;
