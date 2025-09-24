DROP TRIGGER IF EXISTS trigger_timestamps_applications ON applications;
DROP TRIGGER IF EXISTS trigger_set_applied_at ON applications;

DROP FUNCTION IF EXISTS set_applied_at();

DROP TABLE IF EXISTS applications;

DROP TYPE IF EXISTS application_status_t;

