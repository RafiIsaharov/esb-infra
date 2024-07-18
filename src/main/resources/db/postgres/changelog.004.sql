--liquibase formatted sql
--changeset rafi.isaharov:6
--comment: supporting second response
ALTER TABLE routes ADD COLUMN origSecResponse VARCHAR;
ALTER TABLE routes ADD COLUMN transformedSecResponse VARCHAR;
ALTER TABLE routes ADD COLUMN secmatchingId VARCHAR;


