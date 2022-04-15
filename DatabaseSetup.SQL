/*
This file contains the queries neccessary to set up the initial database and tables.
Tables can be filled via the DatabaseInsert.SQL file or via the Java methods.
*/

CREATE DATABASE "Bank_Database"

CREATE TABLE "Account"
(
    account_number integer NOT NULL DEFAULT nextval('test_id_seq12'::regclass),
    amount numeric,
    account_type text NOT NULL,
    opening_date date,
    CONSTRAINT account_number PRIMARY KEY (account_number)
)

CREATE TABLE "Client"
(
    client_number integer NOT NULL DEFAULT nextval('test_id_seq7'::regclass),
    given_name text,
    prefix text,
    last_name text,
    street_name text,
    house_number integer,
    zip_code integer,
    town text,
    wallet_number integer,
    CONSTRAINT client_number PRIMARY KEY (client_number)
)

CREATE TABLE "Transactions"
(
    transaction_id integer NOT NULL DEFAULT nextval('test_id_seq5'::regclass),
    description text,
    amount integer,
    date_of_creation date,
    date_of_execution date,
    account_number_sender integer,
    account_number_recipient integer,
    CONSTRAINT transaction_id PRIMARY KEY (transaction_id),
    CONSTRAINT account_number_recipient FOREIGN KEY (account_number_recipient) REFERENCES "Account" (account_number),
    CONSTRAINT account_number_sender FOREIGN KEY (account_number_sender) REFERENCES "Account" (account_number),
    CONSTRAINT loan_id FOREIGN KEY (loan_id) REFERENCES "Outstanding_loan" (loan_id)
)

CREATE TABlE "Stored_Transactions"
(
    transaction_id integer NOT NULL DEFAULT nextval('test_id_seq6'::regclass),
    description text,
    amount integer,
    date_of_creation date,
    date_of_execution date,
    account_number_sender integer,
    account_number_recipient integer,
    CONSTRAINT stored_transaction_id PRIMARY KEY (transaction_id),
    CONSTRAINT stored_account_number_recipient FOREIGN KEY (account_number_sender_stored) REFERENCES "Account" (account_number),
    CONSTRAINT stored_account_number_sender FOREIGN KEY (account_number_recipient_stored) REFERENCES "Account" (account_number),
    CONSTRAINT loan_id FOREIGN KEY (loan_id) REFERENCES "Outstanding_loan" (loan_id)
)

CREATE TABLE "Wallet DEFAULT nextval('test_id_seq9'::regclass)"
(
    wallet_number integer NOT NULL,
    account_number_current integer,
    account_number_savings integer,
    account_number_invest integer,
    CONSTRAINT wallet_number PRIMARY KEY (wallet_number),
    CONSTRAINT account_number_current FOREIGN KEY (account_number_current) REFERENCES "Account" (account_number),
    CONSTRAINT account_number_invest FOREIGN KEY (account_number_invest) REFERENCES "Account" (account_number),
    CONSTRAINT accoutn_number_savings FOREIGN KEY (account_number_savings) REFERENCES "Account" (account_number)
)

CREATE TABLE "Outstanding_Loan"
(
    loan_id integer NOT NULL DEFAULT nextval('test_id_seq'::regclass),
    account_number integer,
    contra_account integer,
    contract_date date,
    maturity_date date,
    original_amount numeric,
    payment_interval_amount numeric,
    remaining_amount numeric,
    payment_interval_days numeric,
    date_next_installment date,
    CONSTRAINT loan_id PRIMARY KEY (loan_id),
    CONSTRAINT account_number FOREIGN KEY (account_number_loaner) REFERENCES "Account" (account_number),
    CONSTRAINT account_number_contra FOREIGN KEY (account_number_contra) REFERENCES "Account" (account_number)
)
