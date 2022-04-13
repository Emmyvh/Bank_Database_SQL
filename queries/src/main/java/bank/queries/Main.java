package bank.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        Client client = new Client();
        Wallet wallet = new Wallet();
        Transactions transactions = new Transactions();
        Loan loan = new Loan();

        loan.payInstalment(1);
    }
}
