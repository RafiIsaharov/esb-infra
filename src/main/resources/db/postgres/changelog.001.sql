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
        targetId VARCHAR(250),
        origResponse VARCHAR,
        transformedResponse VARCHAR,
        timestamp TIMESTAMP default NOW(),
        PRIMARY KEY (uuid)
        );