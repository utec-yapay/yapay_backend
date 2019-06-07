-- sudo -u postgres psql

/*
CREATE database YaPayDB;
\c YaPayDB
*/

CREATE TABLE payments (
	payment_id uuid,
	company_id uuid,
	company_name varchar(225),
	total float,
	payment_date timestamp,
	confirmed bool,
	PRIMARY KEY (payment_id)
);

