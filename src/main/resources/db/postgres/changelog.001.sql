--liquibase formatted sql
--changeset rafi.isaharov:1
--comment: initial structure
CREATE TABLE IF NOT EXISTS routes (
    uuid VARCHAR(255) DEFAULT gen_random_uuid(),
    contextId VARCHAR(250) NOT NULL,
    sourceName VARCHAR(250) NOT NULL,
    sourceId VARCHAR(250) NOT NULL,
    origRequest VARCHAR NOT NULL,
    transformedRequest VARCHAR,
    targetName VARCHAR(250) NOT NULL,
    matchingId VARCHAR(250), -- renamed from targetId
    origResponse VARCHAR,
    transformedResponse VARCHAR,
    origSecResponse VARCHAR, -- added for supporting second response
    transformedSecResponse VARCHAR, -- added for supporting second response
    secmatchingId VARCHAR, -- added for supporting second response
    timestamp TIMESTAMP DEFAULT NOW(), -- combined alteration
    PRIMARY KEY (uuid)
    );