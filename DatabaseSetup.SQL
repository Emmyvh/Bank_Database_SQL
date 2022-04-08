/*
This file contains the queries neccessary to set up the initial database and tables.
Tables can be filled via the DatabaseInsert.SQL file or via the Java methods.
*/

CREATE DATABASE "Bank_Database"

CREATE TABLE "Account"
(
    account_number integer NOT NULL,
    amount numeric,
    account_type text NOT NULL,
    opening_date date,
    client_number integer NOT NULL,
    transaction_id integer,
    CONSTRAINT account_number PRIMARY KEY (account_number),
    CONSTRAINT client_number_owner FOREIGN KEY (client_number) REFERENCES "Client" (client_number),
    CONSTRAINT transaction_id FOREIGN KEY (transaction_id) REFERENCES "Transaction" (transaction_id)
)

CREATE TABLE "Client"
(
    client_number integer NOT NULL,
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

CREATE TABLE "Loan"
(
    loan_id integer NOT NULL,
    client_number integer,
    account_number integer,
    contra_account integer,
    contract_date date,
    maturity_date date,
    amount money,
    payment_interval integer,
    CONSTRAINT loan_id PRIMARY KEY (loan_id),
    CONSTRAINT account_number FOREIGN KEY (account_number) REFERENCES "Account" (account_number),
    CONSTRAINT account_number_contra FOREIGN KEY (contra_account) REFERENCES "Account" (account_number) ,
    CONSTRAINT client_number_owner FOREIGN KEY (client_number) REFERENCES "Client" (client_number)
)

CREATE TABLE "Transaction"
(
    transaction_id integer NOT NULL,
    transaction_type text,
    description text,
    amount integer,
    date_of_creation date,
    date_of_execution date,
    client_number_sender integer,
    client_number_recipient integer,
    CONSTRAINT transaction_id PRIMARY KEY (transaction_id),
    CONSTRAINT account_number_recipient FOREIGN KEY (client_number_recipient) REFERENCES "Account" (account_number),
    CONSTRAINT account_number_sender FOREIGN KEY (client_number_sender) REFERENCES "Account" (account_number)
)

CREATE TABlE "Stored_Transactions"
(
    transaction_id integer NOT NULL,
    transaction_type text,
    description text,
    amount integer,
    date_of_creation date,
    date_of_execution date,
    client_number_sender integer,
    client_number_recipient integer,
    CONSTRAINT stored_transaction_id PRIMARY KEY (transaction_id),
    CONSTRAINT stored_account_number_recipient FOREIGN KEY (client_number_recipient) REFERENCES "Account" (account_number),
    CONSTRAINT stored_account_number_sender FOREIGN KEY (client_number_sender) REFERENCES "Account" (account_number)
)

CREATE TABLE "Wallet"
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
