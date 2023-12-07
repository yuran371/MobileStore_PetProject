CREATE DATABASE market_repository;

CREATE SCHEMA market;
SHOW search_path;
SET search_path TO market;

CREATE TABLE Items (item_id BIGSERIAl PRIMARY KEY,
					model VARCHAR(32) NOT NULL,
					brand VARCHAR(32) NOT NULL,
					attributes VARCHAR(128) NOT NULL,
					price NUMERIC(12,2) CHECK (price > 0),
					quantity INT CHECK (quantity >= 0) NOT NULL );
					

CREATE TABLE IF NOT EXISTS personal_account (
	login VARCHAR(32) PRIMARY KEY,
	full_name VARCHAR(64) NOT NULL,
	country VARCHAR(64) NOT NULL,
	city VARCHAR(256) NOT NULL,
	address VARCHAR(256) NOT NULL,
	phone_number TEXT CONSTRAINT phone_number_constraint NOT NULL,
	CONSTRAINT phone_number_constraint CHECK (((phone_number LIKE '+7[0-9]{10}$') AND city = 'russia') OR city != 'russia')
);


CREATE TABLE IF NOT EXISTS sell_history (
	sell_id BIGSERIAl PRIMARY KEY,
	item_id BIGINT REFERENCES Items (item_id) ON UPDATE CASCADE ON DELETE RESTRICT,
	login VARCHAR(32) NOT NULL, 
	FOREIGN KEY (login) REFERENCES personal_account (login) ON DELETE RESTRICT,
	quantity INT CHECK (quantity >= 0) NOT NULL,
	sell_date TIMESTAMPTZ NOT NULL  
);

CREATE INDEX IF NOT EXISTS item_id_idx ON sell_history (item_id);

CREATE INDEX IF NOT EXISTS login_idx ON sell_history (login);


 
