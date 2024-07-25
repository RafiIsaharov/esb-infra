--liquibase formatted sql
--changeset rafi.isaharov:3
--comment: create trigger to execute update_timestamp function on every update
CREATE TRIGGER update_routes_timestamp
    BEFORE UPDATE ON routes
    FOR EACH ROW
    EXECUTE FUNCTION set_timestamp();