CREATE DATABASE market_repository;

CREATE SCHEMA market;
SHOW search_path;
SET search_path TO market;

CREATE TABLE Items (item_id BIGSERIAl PRIMARY KEY,
					model VARCHAR(32) NOT NULL,
					brand VARCHAR(32) NOT NULL,
					attributes VARCHAR(128) NOT NULL,
					price NUMERIC(12,2) CHECK (price > 0),
					quantity INT CHECK (quantity >= 0)) NOT NULL;
				
DROP TABLE Items;
					
