--liquibase formatted sql
--changeset rafi.isaharov:5
--comment: renaiming targetId to matchingId
ALTER TABLE routes RENAME COLUMN targetId TO matchingId;


