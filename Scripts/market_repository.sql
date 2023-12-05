CREATE DATABASE market_repository;

CREATE SCHEMA market;
SHOW search_path;
SET search_path TO market;

CREATE TABLE IF NOT EXISTS personal_account (
	login VARCHAR(32) PRIMARY KEY,
	full_name VARCHAR(64) NOT NULL,
	country VARCHAR(64) NOT NULL,
	city VARCHAR(256) NOT NULL,
	address VARCHAR(256) NOT NULL,
	phone_number TEXT CONSTRAINT phone_number_constraint NOT NULL,
	CONSTRAINT phone_number_constraint CHECK (((phone_number LIKE '+7[0-9]{10}$') AND city = 'russia') OR city != 'russia')
);

