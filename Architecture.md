# Architecture

## Project overview
A project to practice working with SQL queries and PostgreSQL.
It simulates a banking system which contains clients, wallets, accounts, transactions, and loans.

## Project architecture
The project consists of an API layer and a database.
The API layer is written in Java and build via Gradle.
The database is build in PostgreSQL.
To run the project a PostgreSQL Database needs to be present on the local machine.
To build a database you first need to download and install PostgreSQL.
Next you will need to build the database itself via either PostgreSQL's Admin interface or by hand using the SQL code provided in DatabaseSetup.SQL and DatabaseInsert.SQL.
These steps will add severall clients, accounts, and wallets, thus providing you with a basic database on whih queries can be executed.
To add transactions between accounts and loans, use the Java methods.
