--liquibase formatted sql
--changeset rafi.isaharov:2
--comment: create function to update timestamp on every update
CREATE OR REPLACE FUNCTION set_timestamp()
RETURNS TRIGGER AS '
BEGIN
    NEW.timestamp = NOW();
    RETURN NEW;
END;
' LANGUAGE plpgsql;