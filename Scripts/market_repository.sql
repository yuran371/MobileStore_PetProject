CREATE DATABASE market_repository;

CREATE SCHEMA market;
SHOW search_path;

SET search_path TO market;
CREATE TABLE items
(
    item_id                   BIGSERIAl PRIMARY KEY,
    brand                     VARCHAR(32)  NOT NULL,
    model                     VARCHAR(32)  NOT NULL,
    internal_memory           INT          NOT NULL,
    RAM                       INT          NOT NULL,
    color                     VARCHAR(32)  NOT NULL,
    OS                        VARCHAR(32)  NOT NULL,
    image                     VARCHAR(124) NOT NULL,
    item_sales_information_id BIGINT       NOT NULL UNIQUE REFERENCES item_sales_information (id),
    UNIQUE (model, internal_memory, RAM, color)
);

CREATE TABLE item_sales_information
(
    id       BIGSERIAl PRIMARY KEY,
    price    NUMERIC(12, 2) CHECK (price > 0),
    currency VARCHAR(1)                NOT NULL,
    quantity INT CHECK (quantity >= 0) NOT NULL
);


ALTER ROLE dmitry SET search_path = market;

CREATE EXTENSION pgcrypto;

CREATE TABLE IF NOT EXISTS personal_account
(
    id           BIGSERIAL    NOT NULL UNIQUE,
    email        VARCHAR(128) PRIMARY KEY,
    password     TEXT         NOT NULL,
    name         VARCHAR(32)  NOT NULL,
    surname      VARCHAR(32)  NOT NULL,
    image        VARCHAR(124) NOT NULL,
    birthday     DATE         NOT NULL,
    countryEnum  VARCHAR(64)  NOT NULL,
    city         VARCHAR(256) NOT NULL,
    address      VARCHAR(256) NOT NULL,
    phone_number TEXT
        CONSTRAINT phone_number_constraint NOT NULL,
    genderEnum   VARCHAR(12)  NOT NULL,
    CONSTRAINT birthday_constraint CHECK (DATE_PART('year', current_date) - DATE_PART('year', birthday) > 18),
    CONSTRAINT phone_number_constraint CHECK (((phone_number ~ '\+7[0-9]{10}$') AND
                                               (countryEnum = 'russia' OR countryEnum = 'россия'))
        OR (countryEnum != 'russia' AND countryEnum != 'россия'))
);


CREATE TABLE IF NOT EXISTS sell_history
(
    sell_id   BIGSERIAl PRIMARY KEY,
    item_id   BIGINT REFERENCES Items (item_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    user_id   BIGINT,
    FOREIGN KEY (user_id) REFERENCES personal_account (id) ON DELETE RESTRICT,
    quantity  INT CHECK (quantity >= 0) NOT NULL,
    price     NUMERIC(12, 2) CHECK (price > 0),
    sell_date TIMESTAMPTZ               NOT NULL
);

CREATE TABLE IF NOT EXISTS profile_info
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT UNIQUE REFERENCES personal_account (id),
    language     VARCHAR(2),
    special_info VARCHAR(256)
);
drop table profile_info;
drop table sell_history;

CREATE TABLE IF NOT EXISTS user_payment_options
(
    account_id         BIGINT REFERENCES personal_account (id) ON DELETE CASCADE,
    added_payment_type VARCHAR(128) NOT NULL,
    PRIMARY KEY (account_id, added_payment_type)
);
DROP TABLE user_payment_options;



CREATE INDEX IF NOT EXISTS item_id_idx ON sell_history (item_id);

ALTER SEQUENCE items_item_id_seq RESTART WITH 1;
TRUNCATE TABLE items CASCADE;
DROP TABLE items CASCADE;

DROP TABLE personal_account CASCADE;
DROP TABLE sell_history CASCADE;


