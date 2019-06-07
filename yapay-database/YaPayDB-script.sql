-- sudo -u postgres psql

/*
CREATE database YaPayDB;
\c YaPayDB
*/

CREATE TABLE public.payments
(
    payment_id uuid NOT NULL,
    company_id uuid NOT NULL,
    company_name character varying(225)  NOT NULL,
    total double precision NOT NULL,
    payment_date timestamp without time zone NOT NULL,
    confirmed boolean NOT NULL DEFAULT false,
    CONSTRAINT payments_pkey PRIMARY KEY (payment_id)
)
